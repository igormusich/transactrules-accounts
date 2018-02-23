package com.transactrules.accounts;

import com.transactrules.accounts.metadata.domain.AccountType;
import com.transactrules.accounts.utilities.Utility;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class UtilitiesTest extends BaseIntegrationTest {

    @Test
    public void checksumTest() throws IOException, NoSuchAlgorithmException {
        AccountType loanAccountType = TestConfiguration.createLoanGivenAccountType();

        BigInteger checksum1 = Utility.checksum(loanAccountType);

        loanAccountType.setClassName(loanAccountType.getClassName()+"1");

        BigInteger checksum2 = Utility.checksum(loanAccountType);

        assertThat(checksum1, not(is(checksum2)));

    }
}
