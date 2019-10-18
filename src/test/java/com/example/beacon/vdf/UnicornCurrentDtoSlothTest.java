package com.example.beacon.vdf;

import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;

public class UnicornCurrentDtoSlothTest {

    private static final Logger logger = LoggerFactory.getLogger(VdfUnicornService.class);

    @Test
    public void testVdfSloth() {
        int iterations = 10;

        String x1 = "1404043690629380907225335546537072240138452033482682978653709920833508392304604779405049507673218509920214876808984370444512904269659903953893724698970227";
        BigInteger y1 = VdfSloth.mod_op(new BigInteger(x1), iterations);
        System.out.println("y1:" + y1);

        boolean b1 = VdfSloth.mod_verif(y1, new BigInteger(x1), 10);
        System.out.println(b1);

        String x2 = "10513531349213524528308273595862543424331204518596323927705455877377838285627879541326447725170980613332871249345955821936741663537033130006675205257763371";
        BigInteger y2 = VdfSloth.mod_op(new BigInteger(x2), iterations);
        System.out.println("y2:" + y2);

        boolean b2 = VdfSloth.mod_verif(y2, new BigInteger(x2), 10);
        System.out.println(b2);

        String x3 = "10893863243894043779135616321778625368455310435028551601071697462018347952009212797828726307881439778513485521440424865009746643780290776664161508987293011";
        BigInteger y3 = VdfSloth.mod_op(new BigInteger(x3), iterations);
        System.out.println("y3:" + y3);

        boolean b3 = VdfSloth.mod_verif(y3, new BigInteger(x3), 10);
        System.out.println(b3);

        String x4 = "8172997724128768607836703758283340171365537171039216224808675324911242242893628654878825158859900470643495664805479201216692478122892771307442460586450483";
        BigInteger y4 = VdfSloth.mod_op(new BigInteger(x4), iterations);
        System.out.println("y4:" + y4);

        boolean b4 = VdfSloth.mod_verif(y4, new BigInteger(x4), 10);
        System.out.println(b4);

        String x5 = "12883743427730472174983249083648262672111950686303285737479481651912251175496309782377795231403982170293815714443312863417623949779691678972961576702317844";
        BigInteger y5 = VdfSloth.mod_op(new BigInteger(x5), iterations);
        System.out.println("y5:" + y5);

        boolean b5 = VdfSloth.mod_verif(y5, new BigInteger(x5), 10);
        System.out.println(b5);

        String x6 = "4960205710156247156456168345127940463607947358116608304865425051820993527554223479701937259869115481771524988174988225380375161612323180248416409690329599";
        BigInteger y6 = VdfSloth.mod_op(new BigInteger(x6), iterations);
        System.out.println("y6:" + y6);

        boolean b6 = VdfSloth.mod_verif(y6, new BigInteger(x6), 10);
        System.out.println(b6);

        String x7 = "723920245827210330710571930189147084766575680729191669369781468565716325629662978509949355527916347475295284359275643718196626872161621364872891182969113";
        String x7Modificado = "823920245827210330710571930189147084766575680729191669369781468565716325629662978509949355527916347475295284359275643718196626872161621364872891182969113";


        BigInteger y7 = VdfSloth.mod_op(new BigInteger(x7), iterations);
        System.out.println("y7:" + y7);

        boolean b7 = VdfSloth.mod_verif(y7, new BigInteger(x7Modificado), 10);
        System.out.println(b7);
    }

    @Test
    public void generateABigPrimeNumber(){
        BigInteger bigInteger = BigInteger.probablePrime(1024, new SecureRandom());
        System.out.println(bigInteger);
    }

    @Test
    public void testChile(){
        BigInteger p = new BigInteger("4d2320693ce73fb525037774084595522500cbe0ff7294b9a8e1e66dd9297a2296c253e9749206e5e4d340bdff0c1e8397fca14bbcc380a9885eaeb6971e4d50996b21e780e08deef4496f4bd4b8f448e8b5f8a052f983309345c1d1d126d113a46406e2defc74cae5df1835b1084d1efcc77f9c9ca0ec0e6fca6abcf31438d7d41cc8d00f33b251b50a7829612e2549663e77ad16ca4a8162db66852bfe0269cb54486e22be8b3d275330c6b979be744b17cc83b405ef2855e9a067b93a716d40919ff63aa799b36e5a37f95af721c684ce6e823080ccb1a0a3e6acd6bdf5a09226b9b4c1003e9a2618112499682f52027d6bcffdcca9ff8ba2a76dcb832a6e", 16);
        System.out.println(p);
    }

//    @Test
    public void testTimeSloth(){
//        int iterations = 200000;   // 1:14
//        int iterations = 190000;   // 1:10
//        int iterations = 180000;   // 1:08
//        int iterations = 170000;   // 1:02

        int iterations = 170000*3;

        String x1 = "1404043690629380907225335546537072240138452033482682978653709920833508392304604779405049507673218509920214876808984370444512904269659903953893724698970227";

        logger.info("Start");
        BigInteger y1 = VdfSloth.mod_op(new BigInteger(x1), iterations);
        System.out.println("y1:" + y1);
        logger.info("End");
        System.out.println(iterations);
    }

}