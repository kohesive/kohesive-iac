package uy.kohesive.iac.model.aws.codegen.s3

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import java.io.File
import java.io.FileInputStream

class SourceCache(val sourceDir: String) {

    // FQ name -> compilation unit
    private val cache = HashMap<String, CompilationUnit>()

    fun get(classFqName: String): CompilationUnit {
        val classKey = if (classFqName.contains('$')) {
            classFqName.substring(0, classFqName.lastIndexOf('$'))
        } else {
            classFqName
        }

        return cache.getOrPut(classKey) {
            val classFile = File(sourceDir, classKey.replace('.', '/') + ".java")
            if (!classFile.exists()) {
                throw IllegalStateException("Source for $classKey doesn't exist under ${classFile.path}")
            }

            FileInputStream(classFile).use {
                JavaParser.parse(it)
            }
        }
    }

}