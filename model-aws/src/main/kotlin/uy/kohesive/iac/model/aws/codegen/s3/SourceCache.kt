package uy.kohesive.iac.model.aws.codegen.s3

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import java.io.File
import java.io.FileInputStream

class SourceCache(val sourceDir: String) {

    // FQ name -> compilation unit
    private val cache = HashMap<String, CompilationUnit>()

    fun get(classFqName: String): CompilationUnit {
        if (classFqName.contains('$')) {
            throw UnsupportedOperationException("Nested classes not supported yet")
        }

        return cache.getOrPut(classFqName) {
            val classFile = File(sourceDir, classFqName.replace('.', '/') + ".java")
            if (!classFile.exists()) {
                throw IllegalStateException("Source for $classFqName doesn't exist under ${classFile.path}")
            }

            FileInputStream(classFile).use {
                JavaParser.parse(it)
            }
        }
    }

}