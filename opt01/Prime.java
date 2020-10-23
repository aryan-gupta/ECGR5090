
/// This standalone class checks if a number is prime. It uses
/// a few different shorcuts to speed up the calculations
/// @author Aryan Gupta
/// @date 2020-09-11
public final class Prime {
    /// A cache of prime numbers until 100
    /// @note stolen from https://gist.github.com/cblanc/46ebbba6f42f61e60666
    private static final int PRIMES[] = { 2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97 };
    
    /// Checks if a number is prime
    /// @note There are a few shortcuts the algorithm uses. If the number is less than
    ///       the largest number in the prime cache, it uses a binary search to see if
    ///       the number is in the cache or not. Another shortcut it uses is that if a
    ///       number is not divisible by 2, it will never be divisible by 4, 6, 8 or
    ///       any other multiple of 2. In other words, the leaves of a prime factorization
    ///       tree is always prime numbers. So, the algorithm then only checks divisiblity
    ///       by primes from the cache. This will have diminishing returns as the probibility
    ///       of larger numbers being a number in the prime factorization exponentially (i think)
    ///       decreases. Will need testing to see optimum numbers of primes in the cache. The last
    ///       step in the process is brute forcing the remaining numbers. It will check from the
    ///       largest number in the prime cache to half of the value of the number. It will skip
    ///       even numbers because of the statement I made earlier about the prime factorizations. 
    /// @param num The number to check
    /// @return If the number is prime
    public static boolean check_prime(int num) {
        int largest_prime_in_cache = PRIMES[PRIMES.length - 1];

        // first check if the number is in the prime cache
        if (num <= largest_prime_in_cache) {
            int idx = java.util.Arrays.binarySearch(PRIMES, num);
            
            if (idx >= 0) {
                return true;
            }
        }

        // prime factorization rule shortcut, see note.
        // This will significantly cut down the number of
        // modulus ops we have to do. 
        for (int idx = 0; idx < PRIMES.length; ++idx) {
            int mod = PRIMES[idx];
            if (num % mod == 0) {
                return false;
            }
        }

        // we now check the rest of the numbers. Since we are starting at
        // an odd number we can skip by 2's
        for (int div = largest_prime_in_cache + 2; div < num / 2; div += 2) {
            if (num % div == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            boolean is_prime = check_prime(java.lang.Integer.parseInt(args[0]));

            java.lang.System.out.print(args[0]);
            java.lang.System.out.print(" is");
            java.lang.System.out.print( (is_prime)? "" : " not" );
            java.lang.System.out.print(" a prime number\n");
        } else {
            java.lang.System.out.println("USAGE: java <class_name> <num_to_test>");
            java.lang.System.out.println("EX   : java Prime 69");
        }
    }
}