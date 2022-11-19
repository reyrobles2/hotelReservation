package model;

public enum RoomType {
    SINGLE("Single bed", 1),
    DOUBLE("Double bed", 2);

    private final String longText;
    private final int value;

    // Methods
    RoomType(String longText, int value){
        this.longText = longText;
        this.value = value;
    }

    public static RoomType getValues(int value){
        for (RoomType roomType : RoomType.values()) {
            if (roomType.value == value) {
                return roomType;
            }
        }
        throw new IllegalArgumentException("Error - Invalid room type");
    }

    @Override
    public String toString(){
        return this.longText;
    }
}
