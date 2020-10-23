/// This standalone class checks if a word is a palindrome
/// @author Aryan Gupta
/// @date 2020-09-11
public final class Palindrome {
    /// Internal recursive algoritm to check if string is palindrome
    /// @param str The string to check
    /// @param idx the current index being checked
    /// @return If the word is a palindrome
    private static boolean check_palindrome_internal(String str, int idx) {
        if (idx >= str.length() / 2) {
            return true;
        }

        int match_idx = str.length() - idx - 1;

        if (str.charAt(idx) == str.charAt(match_idx)) {
            return check_palindrome_internal(str, idx + 1);
        } else {
            return false;
        }
    }

    /// Public interface to check if a string is a palindrome
    /// @note calls check_palindrome_internal(String, int) internally
    /// @param str The string to check
    /// @return If the word is a palindrome
    public static boolean check_palindrome(String str) {
        return check_palindrome_internal(str, 0);
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            boolean is_palindrome = check_palindrome(args[0]);

            java.lang.System.out.print(args[0]);
            java.lang.System.out.print(" is");
            java.lang.System.out.print( (is_palindrome)? "" : " not" );
            java.lang.System.out.print(" a palindrome\n");
        } else {
            java.lang.System.out.println("USAGE: java <class_name> <word_to_test>");
            java.lang.System.out.println("EX   : java Palindrome racecar");
        }
    }
}