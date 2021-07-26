import java.math.BigInteger;
public class ExtendedEuclidean{
    public static class Triplet{
        BigInteger gcd;
        BigInteger x;
        BigInteger y;

        public Triplet(BigInteger gcd, BigInteger x, BigInteger y){
            this.gcd = gcd;
            this.x = x;
            this.y = y;
        }
    }

    public static Triplet gcd(BigInteger a, BigInteger b){

        if(BigInteger.ZERO.equals(b)){
            return new Triplet(a, BigInteger.ONE, BigInteger.ZERO);
        }
        Triplet ans = extendedEuclideanAlgo(b, a.mod(b));
        return ans;
    }
    public static Triplet extendedEuclideanAlgo(BigInteger a, BigInteger b){

        if(BigInteger.ZERO.equals(b)){
                return new Triplet(a, BigInteger.ONE, BigInteger.ZERO);
        }
        Triplet ans = extendedEuclideanAlgo(b, a.mod(b));

        Triplet result = new Triplet(ans.gcd, ans.y, ans.x.subtract((a.divide(b)).multiply(ans.y)));
        return result;
    }

    public static BigInteger modInverse(BigInteger a, BigInteger m){
        Triplet ans = extendedEuclideanAlgo(a, m);
            return  (ans.x.mod(m).add(m)).mod(m);
    }

}