package com.asocialfingers.mp_list.Backend.Connection;

public class ConnectionLinks {

    private static String address = "mp-list.epizy.com";
    //private static String address = "192.168.1.50";

    public static  String CookieHolder = "__test=84155ebdc9727d1ad5beae12584d0259; expires=1 Ocak 2038 Cuma 02:55:55; path=/";

    public static String getPercentageAndSum = "http://" + address + "/pieces/getPercentageAndSum";

    public static String getPiecesFromCategory = "http://" + address + "/pieces/_CATEGORY_ID_";

    public static String getTotalJunkCount = "http://" + address + "/pieces/getPieceJunkCount";

    public static String insertCategory = "http://" + address + "/categories/insertCategory";

    public static String getTotalPieceCount = "http://" + address + "/pieces/getPieceCount";

    public static String getCategoryId = "http://" + address + "/categories/_CATEGORY_ID_";

    public static String insertVersion = "http://" + address + "/versions/insertVersion";

    public static String getPiecesFromName = "http://" + address + "/piece/_PIECE_NAME_";

    public static String getVersionId = "http://" + address + "/versions/_VERSION_ID_";

    public static String insertModule = "http://" + address + "/modules/insertModule";

    public static String getLastPieces = "http://" + address + "/pieces/lasttenpiece";

    public static String getModuleId = "http://" + address + "/modules/_MODULE_ID_";

    public static String insertBrand = "http://" + address + "/brands/insertBrand";

    public static String insertModel = "http://" + address + "/models/insertModel";

    public static String insertPiece = "http://" + address + "/pieces/insertPiece";

    public static String getBrandId = "http://" + address + "/brands/_BRAND_ID_";

    public static String getModelId = "http://" + address + "/models/_MODEL_ID_";

    public static String getCategories = "http://" + address + "/categories";

    public static String getVersions = "http://" + address + "/versions";

    public static String getModules = "http://" + address + "/modules";

    public static String getBrands = "http://" + address + "/brands";

    public static String getModels = "http://" + address + "/models";

    public static String getPieces = "http://" + address + "/pieces";

    public static String getUsers = "http://" + address + "/users";

    public static String login = "http://" + address + "/login";

}