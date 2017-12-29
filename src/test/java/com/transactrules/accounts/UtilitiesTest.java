package com.transactrules.accounts;

import com.transactrules.accounts.configuration.AccountType;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class UtilitiesTest {

    @Test
    public void checksumTest() throws IOException, NoSuchAlgorithmException {
        AccountType loanAccountType = TestUtility.CreateLoanGivenAccountType();

        BigInteger checksum1 = Utilities.checksum(loanAccountType);

        loanAccountType.setName(loanAccountType.getName()+"1");

        BigInteger checksum2 = Utilities.checksum(loanAccountType);

        assertThat(checksum1, not(is(checksum2)));

    }
}
