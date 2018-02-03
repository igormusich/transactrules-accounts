package com.transactrules.accounts.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class Solver
{
    private  Logger logger = LoggerFactory.getLogger(Solver.class);
    //Maximum allowed number of iterations.
    public  final int ITMAX = 100;
    //Machine floating-point precision.
    public  final  BigDecimal EPS = BigDecimal.valueOf(0.000000003);

    public  BigDecimal FindFunctionZero(Function<BigDecimal, BigDecimal> func, BigDecimal lowerBound, BigDecimal upperBound, BigDecimal tolerance)
    {
        //Using Brent’s method, find the root of a function func known to lie between x1 and x2. The
        //root, returned as zbrent, will be refined until its accuracy is tol.

        int iter = 0;
        BigDecimal a = BigDecimal.ZERO;
        BigDecimal b = BigDecimal.ZERO;
        BigDecimal c = BigDecimal.ZERO;
        BigDecimal d = BigDecimal.ZERO;
        BigDecimal e = BigDecimal.ZERO;
        BigDecimal min1 = BigDecimal.ZERO;
        BigDecimal min2 = BigDecimal.ZERO;

        a = lowerBound;
        b = upperBound;
        c = upperBound;

        BigDecimal fa = BigDecimal.ZERO;
        BigDecimal fb = BigDecimal.ZERO;
        BigDecimal fc = BigDecimal.ZERO;
        BigDecimal p = BigDecimal.ZERO;
        BigDecimal q = BigDecimal.ZERO;
        BigDecimal r = BigDecimal.ZERO;
        BigDecimal s = BigDecimal.ZERO;
        BigDecimal tol1 = BigDecimal.ZERO;
        BigDecimal xm = BigDecimal.ZERO;


        fa = func.apply(a);
        fb = func.apply(b);

        //if ((fa > 0 && fb > 0) || (fa < 0 && fb < 0))
        if ((fa.compareTo(BigDecimal.ZERO) > 0 && fb.compareTo(BigDecimal.ZERO) > 0)
                || (fa.compareTo(BigDecimal.ZERO) < 0 && fb.compareTo(BigDecimal.ZERO) < 0))
        {
            throw new ArithmeticException("No solutions found");
        }

        fc = fb;


        for (iter = 1; iter <= ITMAX; iter++)
        {
            //if ((fb > 0 && fc > 0) || (fb < 0 && fc < 0))
            if ((fb.compareTo(BigDecimal.ZERO) > 0 && fc.compareTo(BigDecimal.ZERO) > 0)
                    || (fb.compareTo(BigDecimal.ZERO) < 0 && fc.compareTo(BigDecimal.ZERO) < 0))
            {
                c = a;
                //Rename a, b, c and adjust bounding interval d
                fc = fa;
                d = b.subtract(a); //d = b - a;
                e = d;
            }

            if (fc.abs().compareTo(fb.abs()) <0)
            {
                a = b;
                b = c;
                c = a;
                fa = fb;
                fb = fc;
                fc = fa;
            }

            //tol1 = 2 * EPS * Math.Abs(b) + 0.5M * tolerance;
            tol1 = BigDecimal.valueOf(2).multiply(EPS).multiply(b.abs()).add(BigDecimal.valueOf(0.5).multiply(tolerance));
            //Convergence check.
            xm = BigDecimal.valueOf (0.5).multiply(c.subtract(b)); //xm = 0.5M * (c - b);


            if ( xm.abs().compareTo(tol1)  <= 0|| fb.compareTo(BigDecimal.ZERO) == 0) //if (Math.Abs(xm) <= tol1 || fb == 0.0M)
            {
                return b;
            }


            if(e.abs().compareTo(tol1) >=0 & fa.abs().compareTo(fb.abs())>0) //if ((Math.Abs(e) >= tol1 & Math.Abs(fa) > Math.Abs(fb)))
            {
                s = fb.divide(fa, 16, RoundingMode.HALF_DOWN); // s = fb / fa;
                //Attempt inverse quadratic interpolation.

                if ((a.compareTo(c) == 0)) //((a == c))
                {
                    p = BigDecimal.valueOf(2).multiply(xm).multiply(s); //p = 2 * xm * s;
                    q = BigDecimal.valueOf( 1).subtract(s); //q = 1 - s;
                }
                else
                {
                    q= fa.divide(fc, 16, RoundingMode.HALF_DOWN); //q = fa / fc;
                    r= fb.divide(fc, 16, RoundingMode.HALF_DOWN); //r = fb / fc;
                    //p = s * (2 * xm * q * (q - r) - (b - a) * (r - 1));
                    p= s.multiply(
                            BigDecimal.valueOf(2).multiply(xm).multiply(q).multiply(
                                    q.subtract(r)
                            )
                            .subtract(
                                    b.subtract(a).multiply(
                                            r.subtract(BigDecimal.ONE)
                                    )
                            )
                    );

                    //q = (q - 1) * (r - 1) * (s - 1);
                    q= q.subtract(BigDecimal.ONE)
                            .multiply(r.subtract(BigDecimal.ONE))
                            .multiply(s.subtract(BigDecimal.ONE));
                }

                if (p.compareTo(BigDecimal.ZERO)>0) //if ((p > 0))
                    q= q.negate(); //q = -q;
                //Check whether in bounds.

                p = p.abs(); //p = Math.Abs(p);

                min1 = BigDecimal.valueOf(3).multiply(xm).multiply(q)
                            .subtract(tol1.multiply(q).abs());// min1 = 3 * xm * q - Math.Abs(tol1 * q);
                min2 = e.multiply(q).abs(); //min2 = Math.Abs(e * q);

                 //if (2 * p < (BigDecimal)(min1 < min2 ? min1 : min2))
                if( BigDecimal.valueOf(2).multiply(p).compareTo(min(min1,min2))< 0)
                {
                    e = d;
                    //Accept interpolation.
                    d = p.divide(q, 16, RoundingMode.HALF_DOWN); //d = p / q;
                }
                else
                {
                    d = xm;
                    //Interpolation failed, use bisection.
                    e = d;
                }

            }
            else
            {
                //Bounds decreasing too slowly, use bisection.
                d = xm;
                e = d;
            }

            a = b;
            //Move last best guess to a.
            fa = fb;

            //Evaluate new trial root.
            //if (Math.Abs(d) > tol1)
            if(d.abs().compareTo(tol1)>0)
            {
                b = b.add(d); //b = b + d;
            }
            else
            {
                b= b.add(MSIGN(tol1,xm)); //b = b + MSIGN(tol1, xm);
            }

            fb = func.apply(b);
            logger.debug(String.format("Iteration %d:f(%s)={%s}", iter, b.setScale(10, RoundingMode.HALF_DOWN).toString(), fb.setScale(10, RoundingMode.HALF_DOWN).toString()));

        }

        throw new ArithmeticException("Maximum number of iterations exceeded");
    }

    private  BigDecimal min(BigDecimal d1, BigDecimal d2){
        return d1.compareTo(d2)<0 ? d1:d2;
    }

    private  BigDecimal MSIGN(BigDecimal a, BigDecimal b)
    {
        //return (decimal)(b >= 0 ? Math.Abs(a) : -Math.Abs(a));

        return (BigDecimal)(b.compareTo(BigDecimal.ZERO) >= 0 ? a.abs() :  a.abs().negate() );
    }

}

