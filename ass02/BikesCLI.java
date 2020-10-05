

public final class BikesCLI {
    public static TableAttributes parseArgs(String[] args) {
        TableAttributes attributes = new TableAttributes();

        if (args.length < 1) {
            return null;
        }

        for (int argidx = 0; argidx < args.length - 1; ++argidx) {
            if (args[argidx].charAt(0) != '-') {
                System.out.println("Invalid argument: " + args[argidx]);
                return null;
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
                    return null;
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
                return null;
        }

        return attributes;
    }
}