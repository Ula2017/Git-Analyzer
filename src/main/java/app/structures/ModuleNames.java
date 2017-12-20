package app.structures;

/**
 * Created by Karol on 2017-12-20.
 */
public enum ModuleNames {
    MODULE1 {
        @Override
        public String toString(){
            return "Monthly ammount of commiters";
        }
    },
    MODULE2 {
        @Override
        public String toString(){
            return "Repository commits";
        }
    };

    public static String getPathForOutput(ModuleNames moduleName){
        return String.format("images/%s.jpg",moduleName.name());
    }
}
