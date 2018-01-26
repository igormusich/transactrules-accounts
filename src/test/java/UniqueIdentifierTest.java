import com.transactrules.accounts.runtime.UniqueId;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UniqueIdentifierTest {
    @Test
    public void testAllocateNextId(){
        UniqueId id = new UniqueId("Account",200L, "ACC-", 8);

        String accountNumber = id.allocateNextId();

        assertThat(accountNumber, is("ACC-00000200"));
    }

}
