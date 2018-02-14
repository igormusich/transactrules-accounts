package com.transactrules.accounts.runtime;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import com.transactrules.accounts.metadata.AccountType;
import groovy.lang.GroovyClassLoader;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
public class CodeGenService {

    @Cacheable(value = "accountClasses", key = "#accountType.className")
    public Class getAccountClass(AccountType accountType)  {
        StringWriter writer = new StringWriter();

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates//groovyAccount.mustache");

        try {
            mustache.execute(writer,accountType).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String code = writer.toString();

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class theParsedClass = groovyClassLoader.parseClass(code);


        //Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, sourceCode);

        return theParsedClass;
    }


    @CacheEvict(value="accountClasses", key="#accountType.className")
    public String evictAccountType(AccountType accountType){
        return accountType.getClassName();
    }

    @CacheEvict(value="accountClasses",  allEntries=true)
    public String evictAllAccountTypes(){
        return "evictAll";
    }

}