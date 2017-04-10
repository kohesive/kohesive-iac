package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.s3.AbstractAmazonS3
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.model.CreateBucketRequest
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonS3(val context: IacContext) : AbstractAmazonS3(), AmazonS3 {

    override fun createBucket(request: CreateBucketRequest): Bucket {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateBucketRequest, Bucket>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateBucketRequest::getBucketName to Bucket::getName
                )
            )
        }
    }

}

class DeferredAmazonS3(context: IacContext) : BaseDeferredAmazonS3(context)
