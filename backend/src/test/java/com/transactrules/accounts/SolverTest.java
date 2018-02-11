package com.transactrules.accounts;

import com.transactrules.accounts.utilities.Solver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

@RunWith(SpringRunner.class)
public class SolverTest {

    @Test
    public void SolveLinear(){

        Solver solver = new Solver();

        BigDecimal tol = BigDecimal.valueOf(0.000001);


        BigDecimal result = solver.FindFunctionZero( this::linear, BigDecimal.valueOf(-3), BigDecimal.valueOf(3), tol);

        assertThat(result.subtract(BigDecimal.valueOf(1.5)).abs().compareTo(tol), lessThan(0));

    }

    @Test
    public void SolveQuadratic(){

        Solver solver = new Solver();

        BigDecimal tol = BigDecimal.valueOf(0.000001);

        BigDecimal result = solver.FindFunctionZero( this::quadratic, BigDecimal.valueOf(2.5), BigDecimal.valueOf(10), tol);

        assertThat(result.subtract(BigDecimal.valueOf(3)).abs().compareTo(tol), lessThan(0));

    }
    public BigDecimal linear(BigDecimal x){
        //2*x -3; root 1.5
        return BigDecimal.valueOf(2).multiply(x).subtract(BigDecimal.valueOf(3));
    }

    public BigDecimal quadratic(BigDecimal x){
        //2x^2-8x+6; roots (1,3)
        return BigDecimal.valueOf(2).multiply(x).multiply(x).subtract(BigDecimal.valueOf(8).multiply(x)).add(BigDecimal.valueOf(6));
    }
}
