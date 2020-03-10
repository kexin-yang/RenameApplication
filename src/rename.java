import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;

class rename {
    static ArrayList<String> actions;
    static ArrayList<String> files;
    static HashMap<String, String> npair;
    public static void main(String[] args) {
        files = new ArrayList<String>();
        actions = new ArrayList<String>();
        npair = new HashMap<String, String>();
        if (args.length == 0) {
            printHelp();
        } else {
            parse(args);
//            System.out.println(actions);
//            System.out.println(files);
            transform(actions,files);
            check(npair);
        }
    }
    // Step1:
    static void parse(String[] args) {
        int argLength = args.length;
        // processing files
        for (int argCount = 0; argCount < argLength; argCount++) {
            // -file
            if (args[argCount].equals("-file")) {
                if (!args[argCount + 1].startsWith("-")) {
                    while (argCount < argLength - 1 && !args[argCount + 1].startsWith("-")) {
                        files.add(args[argCount + 1]);
                        argCount++;
                    }
                } else {
                    System.out.println("Error: -file should take at least one file name.");
                }
            //-replace
            } else if (args[argCount].equals("-replace")) {
                if (args[argCount + 1].startsWith("-")) {
                    System.out.println("Error: your -replace method took 0 parameter, it should take 2 parameters.");
                } else if (args[argCount + 2].startsWith("-")) {
                    System.out.println("Error: your -replace method took 1 parameter, it should take 2 parameters.");
                } else if (!args[argCount + 3].startsWith("-")) {
                    System.out.println("Error: your -replace method took more than 2 parameters, it should take " +
                            "2 parameters");
                } else {
                    actions.add(args[argCount]);   //-replace
                    actions.add(args[argCount + 1]); // 1st para
                    actions.add(args[argCount + 2]); // 2nd para
                }
                argCount = argCount + 2;
            }
            // -help
            else if (args[argCount].equals("-help")) {
                actions.add(args[argCount]);
            }
            //-prefix
            else if (args[argCount].equals("-prefix")) {
                actions.add(args[argCount]);
                if (args[argCount + 1].startsWith("-")) {
                    System.out.println("Error: your -prefix took 0 parameter, it should take at least one parameter.");
                }
                while (argCount < argLength - 1 && !args[argCount + 1].startsWith("-")) {
                    actions.add(args[argCount + 1]);
                    argCount++;
                }
            }
            // -suffix
            else if (args[argCount].equals("-suffix")) {
                actions.add(args[argCount]);
                if (args[argCount + 1].startsWith("-")) {
                    System.out.println("Error: your -suffix took 0 parameter, it should take at least one parameter.");
                }
                while (argCount < argLength - 1 && !args[argCount + 1].startsWith("-")) {
                    actions.add(args[argCount + 1]);
                    argCount++;
                }
            }
            else {
                System.out.println(args[argCount] + " is not a valid option, please choose from the following options.");
                System.out.println("Options:");
                System.out.println("-help                   :: display this help and exit.");
                System.out.println("-prefix [string]        :: rename the file so that it starts with [string].");
                System.out.println("-suffix [string]        :: rename the file so that it ends with [string].");
                System.out.println("-replace [str1] [str2]  :: rename [filename] by replacing all instances of [str1] with [str2].");
                System.out.println("-file [filename]        :: indicate the [filename] to be modified.");
            }
        }
    }
    // Step 2: transfer file names into hashmap of old and new names
    static void transform(ArrayList<String> actions, ArrayList<String> files) {
        int actionLength = actions.size();
        int fileLength = files.size();
    // if not meet minimum
    if (files.isEmpty()){
        System.out.println("Error: The valid option is less than the minimum number of 1.");
        printHelp();
    }
    if (actions.isEmpty()){
        System.out.println("Error: The valid file to operate on is less than the minimum number of 1.");
        printHelp();
    }
    //process @date, @time
    for (int actionCount = 0; actionCount < actionLength; actionCount++){
        if(actions.get(actionCount).equals("@date")){
            String date = String.valueOf(LocalDate.now());
            System.out.println("date is: " + date + "detected @date at index "+ actionCount);
            actions.set(actionCount, date);
        }
        else if (actions.get(actionCount).equals("@time")){
            String time;
            time = String.valueOf(LocalDateTime.now());
            time = time.substring(11, time.length()-7);
            time.replace("/", "-");
            System.out.println("time after processed: " +  time);
            actions.set(actionCount, time);
        }
    }
    // populate the npair hashmap with the same key, value pair
        for (int fileCount = 0; fileCount< fileLength;fileCount++){
           npair.put(files.get(fileCount),files.get(fileCount));
        }
    // help
        for (String action: actions){
            if (action.equals("-help")){
            printHelp();
            System.exit(0);
            }
        }
        for (int actionCount = 0; actionCount < actionLength; actionCount++){

            // prefix
            if (actions.get(actionCount).equals("-prefix")){
                String prefixArg = "";
                while (actionCount + 1 < actionLength && !actions.get(actionCount + 1).startsWith("-"))
                    //find out all prefix make them a string together
                    {prefixArg += actions.get(actionCount+1);
                    actionCount++;
//                    System.out.println("the prefixArg now is: " + prefixArg);
                    }
                for (int fileCount = 0; fileCount< fileLength; fileCount++){
                    // do the prefix for every file
                    String newName = prefixArg + npair.get(files.get(fileCount));
                    npair.put(files.get(fileCount),newName);
                }
            }
            //suffix
            if (actions.get(actionCount).equals("-suffix")){
                String suffixArg = "";
                while (actionCount + 1 < actionLength && !actions.get(actionCount + 1).startsWith("-")){
                    suffixArg += actions.get(actionCount+1);
                    actionCount++;
                }
                for (int fileCount = 0; fileCount < fileLength; fileCount++){
                    //do suffix for every file
                    String newName;
                    newName = npair.get(files.get(fileCount)) + suffixArg;
                    npair.put(files.get(fileCount), newName);
                }
            }
            // replace
            if (actions.get(actionCount).equals("-replace")){
                String str1 = actions.get(actionCount+1);
                String str2 = actions.get(actionCount+2);
                for (int fileCount = 0; fileCount < fileLength; fileCount++){
                    //check if filename contains str1
                    if (!files.get(fileCount).contains(str1)){
                        System.out.println("Error: the file does not contain " + str1);
                    }
                    else {
                        String oldName = files.get(fileCount);
                        String newName = oldName.replace(str1,str2);
                        npair.put(files.get(fileCount),newName);
                        for (Map.Entry<String, String> entry : npair.entrySet()) {
                            System.out.println(entry.getKey() + " " + entry.getValue());
                        }
                    }
                }
                actionCount += 2;
            }
        }
    }
// step3
static void check(HashMap<String,String> npair) {
    // check if old key exist in system
    for (Map.Entry<String, String> entry : npair.entrySet()) {
        String oldFile = entry.getKey();
        String newFile = entry.getValue();
        File file1 = new File(oldFile);
        File file2 = new File(newFile);
        if (!file1.exists()){
            System.out.println("Error: "+ file1.toString() + " does not exist");
        }
        if (file2.exists()){
            System.out.println("Error: "+ file2.toString() + " already exists");
        }
        boolean success = file1.renameTo(file2);
        if (success) {
            System.out.println("Successfully renamed " + file1.toString() + " to " + file2.toString());
        }
        else {
            System.out.println("Error renaming " + file1.toString() + " to " + file2.toString());
        }
    }
}

    static void printHelp() {
        System.out.println("(c) 2020 Kexin Yang. Last revised: Jan.31, 2020");
        System.out.println("Usage: java rename [-option argument1 argument2 ...]");
        System.out.println("Options:");
        System.out.println("-help                   :: display this help and exit.");
        System.out.println("-prefix [string]        :: rename the file so that it starts with [string].");
        System.out.println("-suffix [string]        :: rename the file so that it ends with [string].");
        System.out.println("-replace [str1] [str2]  :: rename [filename] by replacing all instances of [str1] with [str2].");
        System.out.println("-file [filename]        :: indicate the [filename] to be modified.");
    }
}

