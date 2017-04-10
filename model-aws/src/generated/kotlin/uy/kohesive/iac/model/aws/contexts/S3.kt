package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface S3Identifiable : KohesiveIdentifiable {

}

interface S3Enabled : S3Identifiable {
    val s3Client: AmazonS3
    val s3Context: S3Context
    fun <T> withS3Context(init: S3Context.(AmazonS3) -> T): T = s3Context.init(s3Client)
}

open class BaseS3Context(protected val context: IacContext) : S3Enabled by context {

    open fun createBucket(bucketName: String, init: CreateBucketRequest.() -> Unit): Bucket {
        return s3Client.createBucket(CreateBucketRequest(bucketName).apply {
            init()
        })
    }

}

@DslScope
class S3Context(context: IacContext) : BaseS3Context(context) {

}
