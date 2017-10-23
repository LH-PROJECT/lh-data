package com.unitedratings.lhcrm.utils;

import org.apache.commons.math3.util.FastMath;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangyongxin
 */
public final class NormalDistributionRandomGenerator {

    private static boolean NOT_FIRST_TIME = false;
    private static final int IA_1 = 16807;
    private static final long IM_1 = 2147483647;
    private static final double AM_1 = (double) 1 / IM_1;
    private static final long IQ_1 = 127773;
    private static final int IR_1 = 2836;
    private static AtomicLong Seed_1;

    private boolean haveNextNextGaussian;

    private double nextNextGaussian;

    private double nextDouble(){
        if(!NOT_FIRST_TIME){
            Random random = new Random();
            Seed_1 =  new AtomicLong((long) (random.nextDouble() * IM_1));
            NOT_FIRST_TIME = true;
        }
        long k = Seed_1.get() / IQ_1;
        Seed_1.set( IA_1 * (Seed_1.get() - k * IQ_1) - IR_1 * k);
        if(Seed_1.get()<0){
            Seed_1 .set( Seed_1.get() + IM_1);
        }
        return AM_1 * Seed_1.get();
    }

    synchronized public double nextGaussian(){
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1);
            double multiplier = FastMath.sqrt(-2 * FastMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

}
