import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Arrays;

public final class BikesUtil {
    public static TableAttributes parseArgs(String[] args) {
        TableAttributes attributes = new TableAttributes();

        if (args.length < 1) {
            return null;
        }

        for (int argidx = 0; argidx < args.length; ++argidx) {
            if (args[argidx].charAt(0) != '-') {
                System.out.println("Invalid argument: " + args[argidx]);
                return null;
            }

            // if we are on the last argument and its a -XXX then pasrse it
            // as a sort argument otherwise parse it as a normal argument with
            // a value
            if (argidx == (args.length - 1)) {
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
                        return null;
                }
            } else {

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
                        return null;
                }
            }
        }

        // There will be no sql injection on my watch
        if (attributes.type != null) {
            String[] validTypes = { "mountain_bike", "race_bike", "street_bike" };
            if (Arrays.binarySearch(validTypes, attributes.type) < 0) {
                System.out.println("Invalid type: " + attributes.type);
                System.exit(1);
            }
        }

        if (attributes.color != null) {
            String[] validColors = { "black", "blue", "red", "steel" };
            if (Arrays.binarySearch(validColors, attributes.color) < 0) {
                System.out.println("Invalid color: " + attributes.color);
                System.exit(1);
            }
        }

        if (attributes.material != null) {
            String[] validMaterials = { "aluminium", "carbon", "steel" };
            if (Arrays.binarySearch(validMaterials, attributes.material) < 0) {
                System.out.println("Invalid material: " + attributes.material);
                System.exit(1);
            }
        }
        
        return attributes;
    }

    public static String getHost() {
        try {
            return Files.readString(Path.of("./tmp-db-ip")).trim();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static String getPasswd() {
        try {
            return Files.readString(Path.of("./db-root-passwd")).trim();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}