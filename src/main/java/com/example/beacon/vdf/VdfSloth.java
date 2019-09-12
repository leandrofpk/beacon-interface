package com.example.beacon.vdf;

import java.math.BigInteger;

public class VdfSloth {

    private static final BigInteger BIG_INT_QUATRO = new BigInteger("4");
    private static final BigInteger BIG_INT_DOIS = new BigInteger("2");
    private static final BigInteger BIG_INT_UM = new BigInteger("1");
//    private static final BigInteger p = new BigInteger("73237431696005972674723595250817150843");
    private static final BigInteger p = new BigInteger("9325099249067051137110237972241325094526304716592954055103859972916682236180445434121127711536890366634971622095209473411013065021251467835799907856202363"); // foi o q que funcionou melhor
//    private static final BigInteger p = new BigInteger("9330576632752239529193608583311978392587576056917025644585840123283489001952085728043655543380433623240672421574743638792311580499453160809525933929996433");
//    private static final BigInteger p = new BigInteger("12387804199868465467672388435398803318964838052337215529360050072162522261463590439470866656077285347655843491545279916056330233418988994453197400906500983");
//    private static final BigInteger p = new BigInteger("8987088473950826040554811820709523123048103930523927713385726553453340510238354103903933372889201807235051570843599430125510025196806176900966683472553477");

    // witness do chile em hexa
//    private static final BigInteger p = new BigInteger("4d2320693ce73fb525037774084595522500cbe0ff7294b9a8e1e66dd9297a2296c253e9749206e5e4d340bdff0c1e8397fca14bbcc380a9885eaeb6971e4d50996b21e780e08deef4496f4bd4b8f448e8b5f8a052f983309345c1d1d126d113a46406e2defc74cae5df1835b1084d1efcc77f9c9ca0ec0e6fca6abcf31438d7d41cc8d00f33b251b50a7829612e2549663e77ad16ca4a8162db66852bfe0269cb54486e22be8b3d275330c6b979be744b17cc83b405ef2855e9a067b93a716d40919ff63aa799b36e5a37f95af721c684ce6e823080ccb1a0a3e6acd6bdf5a09226b9b4c1003e9a2618112499682f52027d6bcffdcca9ff8ba2a76dcb832a6e", 16);
//    private static final BigInteger p = new BigInteger("9737671057291897167054918011001918854901761579229884877241607314047916593064437003134526827384743163060069938045136967897914188927134767478033231364534083203902571073431346747850477377458850916637286148277137392885929923655323718724237837032707907100402957668032539423567403336696387153990487782073871380766808662699521058485359135354706558356424557761976930311947031232649262167182996674580256672200145497877936650973722870424507920529704820383764607107741972206131278956442369478581662399866003759663467281169772876739963139261998926148021494391109600587493235019647586114221801557864149013544960041100650003769966");


    public static BigInteger mod_op(BigInteger x, int t) {
        for (int i = 0; i < t; i++) {
            x = mod_sqrt_op(x, p);
        }
        return x;
    }

    private static BigInteger mod_sqrt_op(BigInteger x, BigInteger p) {
         BigInteger pmais1duv4 = p.add(BIG_INT_UM).divide( BIG_INT_QUATRO);
         BigInteger y = x.modPow(pmais1duv4,p);
         return y ;
    }

    private static boolean quad_res(BigInteger x, BigInteger p) {
        BigInteger powT = x.modPow(p.subtract(BIG_INT_UM).divide(BIG_INT_DOIS), p);
        return powT.equals(BIG_INT_UM);
    }

    public static boolean mod_verif(BigInteger y, BigInteger x, int t){

        for (int i = 0; i < t; i++) {
            y = y.modPow(BIG_INT_DOIS, p);

            if (!quad_res(y, p)) {
                y = (y.negate()).mod(p);
            }
        }

        if (x.mod(p).equals(y) || (x.negate()).mod(p).equals(y)){
            return true;
        } else {
            return false;
        }
    }

}
