class Main {
    public static void main(String[] args) {
        //Check for valid arguments
        if(args.length != 2) {
            System.out.println("Incorrect number of arguments");
            System.out.println("Example of correct command: java main /test.csv");
            return;
        }
        //Get path of CSV file, assuming that this file is valid
        String filePath = args[1];


    }
}
