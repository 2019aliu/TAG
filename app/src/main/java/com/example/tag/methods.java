package com.example.tag;

public class methods {

    /**
     * Connects item to app and adds item to database
     *
     * @param itemName name of item
     * @param itemDescription description of item
     * @param bluetoothID bluetooth ID of item
     */
    public void registerItem(String itemName, String itemDescription, int bluetoothID) {

    }


    /**
     * Displays a list of the user’s registered items
     *
     * @param itemID item ID
     * @return selected item
     */
    public void displayItems(int itemID) {

    }


    /**
     * Deletes item from item list
     *
     * @param itemID item ID
     */
    public void deleteItem(int itemID) {

    }


    /**
     * Updates details of item
     *
     * @param itemID item ID
     */
    public void updateItem(int itemID) {

    }


    /**
     * Displays the item that the user selects
     *
     * @param itemID item ID
     */
    public void displaySelectedItem(int itemID) {

    }


    /**
     * Returns location of item
     *
     * @param itemID item ID
     * @return item coordinates
     */
    public int[] getItemLocation(int itemID) {
        return null;
    }


    /**
     * Returns location of user
     *
     * @param itemID item ID
     * @return user coordinates
     */
    public int[] getUserLocation(int itemID) {
        return null;
    }


    /**
     * Displays directions from user to item
     *
     * @param itemCoordinates item coordinates
     * @param userCoordinates user coordinates
     */
    public void createDirections(int[] itemCoordinates, int[] userCoordinates) {

    }


    /**
     * If bluetooth is in range, call askBTPermissions() to activate Bluetooth
     *
     * @param itemCoordinates item coordinates
     * @param userCoordinates user coordinates
     * @return bluetooth usability
     */
    public boolean bluetoothInRange(int[] itemCoordinates, int[] userCoordinates) {
        return false;
    }


    /**
     * If Wifi is in range, call askBTPermissions() to activate Wifi
     *
     * @param itemCoordinates item coordinates
     * @param userCoordinates user coordinates
     * @return wifi usability
     */
    public boolean wifiInRange(int[] itemCoordinates, int[] userCoordinates) {
        return false;
    }


    /**
     * Asks user permission to enable Bluetooth
     *
     * @return permission given for Bluetooth
     */
    public boolean askBTPermissions() {
        return false;
    }


    /**
     * Turns on Bluetooth
     *
     */
    public void activateBT() {

    }


    /**
     * Asks user permission to enable Wifi
     *
     * @return permission given for Wifi
     */
    public boolean askWFPermissions() {
        return false;
    }


    /**
     * Turns on Wifi
     *
     */
    public void activateWF() {

    }


    /**
     * Turns on sound when within range of Bluetooth/Wifi
     *
     */
    public void beepSound() {

    }


    /**
     * Allows the user to confirm that they found their item
     *
     * @param inputConfirm user selection for confirmation
     * @return found or not found
     */
    public boolean confirmFindings(boolean inputConfirm) {
        return false;
    }


    /**
     * Exits maps and goes back to ‘Display Items’ screen
     *
     */
    public void exitMaps() {

    }
}
