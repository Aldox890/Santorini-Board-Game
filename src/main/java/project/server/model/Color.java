package project.server.model;

public enum Color {
    RED,GREEN,YELLOW;

    public String getColor(){
        if(this.equals(RED)){
            return "\u001B[31m";
        }
        if (this.equals(GREEN)){
            return "\u001B[32m";
        }
        if(this.equals(YELLOW)){
            return "\u001B[33m";
        }
        return "";
    }
}


