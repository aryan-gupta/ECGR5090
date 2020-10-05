// import java.lang.Integer;
// import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Files;
import java.sql.SQLException;

public final class ShowBikes {
    private static void printHowTo() {
        java.io.PrintStream out = java.lang.System.out;
        out.println("ShowBikes –type mountain_bike –gear 5 –wheelbase");
        throw new Error("Show me StackTrace");
    }

    public static void main(String[] args) {
        TableAttributes attributes = new TableAttributes();

        if (args.length < 1) {
            printHowTo();
            return;
        }

        for (int argidx = 0; argidx < args.length - 1; ++argidx) {
            if (args[argidx].charAt(0) != '-') {
                System.out.println("Invalid argument: " + args[argidx]);
                printHowTo();
                return;
            }

            switch (args[argidx].toLowerCase()) {
                case "-type":
                    attributes.type = args[++argidx];
                    continue;

                case "-gear":
                    attributes.gears = Integer.parseInt(args[++argidx]);
                    continue;
                    
                case "-wheelbase":
                    attributes.wheelBase = Integer.parseInt(args[++argidx]);
                    continue;
                    
                case "-height":
                    attributes.height = Integer.parseInt(args[++argidx]);
                    continue;
                    
                case "-color":
                    attributes.color = args[++argidx];
                    continue;
                    
                case "-material":
                    attributes.material = args[++argidx];
                    continue;
                    
                default:
                    printHowTo();
                    return;
            }
        }

        switch (args[args.length - 1].toLowerCase()) {
            case "-type":
                attributes.sort = TableAttributes.ATTRIBUTES.TYPE;
                break;

            case "-gear":
                attributes.sort = TableAttributes.ATTRIBUTES.GEARS;
                break;

            case "-wheelbase":
                attributes.sort = TableAttributes.ATTRIBUTES.WHEEL_BASE;
                break;

            case "-height":
                attributes.sort = TableAttributes.ATTRIBUTES.HEIGHT;
                break;

            case "-color":
                attributes.sort = TableAttributes.ATTRIBUTES.COLOR;
                break;

            case "-material":
                attributes.sort = TableAttributes.ATTRIBUTES.MATERIAL;
                break;

            default:
                printHowTo();
                return;
        }

        String hostip, passwd;
        try {
            hostip = Files.readString(Path.of("./tmp-db-ip")).trim();
            passwd = Files.readString(Path.of("./db-root-passwd")).trim();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(hostip + "  " + passwd);

        SearchTable st;
        try {
            st = new SearchTable(hostip, passwd);
        } catch (SQLException e) {
            System.out.println("Error with connection: " + e.getMessage());
            return;
        }

        st.run(attributes);
    }
}