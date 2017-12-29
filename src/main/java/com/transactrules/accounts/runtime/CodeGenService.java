package com.transactrules.accounts.runtime;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.transactrules.accounts.configuration.AccountType;
import net.openhft.compiler.CompilerUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class CodeGenService {

    @Cacheable(value = "accountValuationClasses", key = "#accountType.name")
    public Class getAccountValuationClass(AccountType accountType) throws ClassNotFoundException, IOException {
        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates//accountValuation.mustache");

        mustache.execute(writer,accountType).flush();

        String className = "com.transactrules.accounts.runtime." + accountType.getName() + "Valuation";

        Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, writer.toString());

        return aClass;
    }

    @CacheEvict(value="accountValuationClasses", key="#accountType.name")
    public String evictAccountType(AccountType accountType){
            return accountType.getName();
    }

    @CacheEvict(value="accountValuationClasses",  allEntries=true)
    public String evictAllAccountTypes(){
        return "evictAll";
    }
}