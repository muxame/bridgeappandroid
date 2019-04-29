package net.bridgeint.app.utils;

import net.bridgeint.app.R;
import net.bridgeint.app.application.Application;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class Constants {

    // Registration Const
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String TYPE = "type";
    public static final String DEVIC_TYPE = "deviceType";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String APPLY_ID = "applyId";
    public static final String APPLIES_ID = "applies_id";
    public static final String FEESOPTIONS = "feesOption";

    // Get Pic Code Const
    public static final String USER_ID = "userId";
    public static final String ACCESS_KEY = "accessKey";
    public static final String PHONE = "phone";
    public static final String NOTIFICATION = "notifStatus";
    public static final String API_KEY = "API_KEY";


    public static final String PAYMENT_COMMENT = "payment_comments";

    // Responces Const
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String KEYWORD = "keyword";
    public static final String BANNER = "Banner";

    // Verify Pic Code Const
    public static final String SMS_CODE = "smsCode";


    public static final String UNIVERSITY = "university";

    // Get Course Const
    public static final String DEGREE_ID = "degreeId";
    public static final String COURSE_TYPE = "courseType";

    // Fragments Const
    public static final int DEGREE_FRAG = 0;
    public static final int COURSE_FRAG = 1;
    public static final int COUNTRY_FRAG = 2;

    // Get Countries By Course & Degree Const
    public static final String COURSE_ID = "courseId";
    public static final String STREM_ID = "stream_id";
    public static final String NEXT_PAGE = "currentpage";
    public static final String LANGUAGE = "language";
    public static final String COMPLETED = "Admission complete";
    public static final String DUE_FEE = "Due for fees";
    public static final String CHAT_TYPE = "CHAT-TEXT";
    public static final int REQUEST_CODE_APPLICATION = 500;
    public static String PACKAGEID = "package";
    public static String CHECKOUTID = "checkout";

    public static int MOVE_TO_RESULT = 1001;
    public static int MOVE_TO_APPLY = 1002;
    public static final int REQUEST_IMAGE_CAPTURE = 1003;
    public static final int REQUEST_IMAGE_GALLERY = 1004;
    public static int MOVE_TO_DASHBOARD = 1005;

    // Get University By Course & Degree Const
    public static final String COUNTRY_CODE = "countryCode";

    // Image URL
    public static final String IMAGE_URL = "http://188.226.178.195/tryAndApply/uploads/";
    public static final String BUNDLE_IMAGE_URL = "http://188.226.178.195/tryAndApply/images/";
//    public static final String IMAGE_URL = "http://dvice.be/tryAndApply/uploads/";
//    public static final String IMAGE_URL1 = "http://dvice.be/tryAndApply/images/";


    // Change Password Const
    public static final String OLD_PASSWORD = "oldPass";
    public static final String NEW_PAWWROD = "password";

    // Upload Image Const
    public static final String IMAGE = "image";
    //
    public static String eventFocus = "";

    // Document Type Const
    public static final String High_School_Transcript =  Application.getAppInstance().getString(R.string.high_school_transcript);
    public static final String Recommendation_Letters = "Recommendation Letters";
    public static final String Passport_Copy = "Passport Copy";
    public static final String English_Proficiency_Test = "English Proficiency Test";
    public static final String Personal_Statement = "Personal Statement";
    public static final String Extra_Documents = "Extra Documents";

    // Apply For Application Const
    public static final String COUNTRY_ID = "countryId";
    public static final String UNIVERSITY_ID = "universityId";
//    public static final String UNIVERSITY_ID = "universityId";

    public static final String Eng_Proficieny_Tests_KEY = "eng_proficieny_tests";
    public static final String Recommendation_Letters_KEY = "recommendation_letters";
    public static final String High_School_Transcripts_KEY = "high_school_transcripts";
    public static final String Passport_Copies_KEY = "passport_copies";
    public static final String Personal_Statement_KEY = "personal_statement";
    public static final String Extra_Document_KEY = "extra_documents";
    public static final String packages_id = "packages_id";
    public static final String price = "price";
    public static final String name = "name";
    public static final String qty = "qty";
    public static final String success_payments_id = "success_payments_id";
    public static final String payment_status = "payment_status";
    public static final String apply_type = "applytype";

    public static final String APPROVED = "APPROVED";
    public static final String PENDING = "PENDING";
    public static final String REJECTED = "Rejected";
    public static final String UNDER_REVIEW = "Under Review";
    public static final String STUDENT_CONST = "Student";
    public static final String UNIVERSITY_CONST = "University";

    // Chat Const
    public static final int IMAGE_TYPE = 12;
    public static final int AUDIO_TYPE = 13;
    public static final int TEXT_TYPE = 14;
    public static final int MESSAGE_STATE_PENDING = 0;
    public static final int MESSAGE_STATE_SENT = 1;
    public static final int MESSAGE_STATE_RECIEVED = 2;
    public static final int MESSAGE_STATE_READ = 3;

    public static final String TO_USER = "toUser";
    public static final String FROM_USER = "fromUser";
    public static final String TEMP_ID = "tempId";


    //Socket Const
    public static final String SOCKET_EVENT_CONNECTED = "appConnected";
    public static final String SOCKET_EVENT_SEND_MESSAGE = "sendMessage";
    public static final String SOCKET_EVENT_SEND_MESSAGE_SERVER = "sendMessage_Server";
    public static final String SOCKET_EVENT_STATE_TYPING_SERVER = "stateTyping_Server";
    public static final String SOCKET_EVENT_STATE_TYPING = "stateTyping";
    public static final String SOCKET_EVENT_MESSAGE_RECEIVED = "messageReceived";

    public static final String SOCKET_EVENT_ADMIN_LIVE = "adminLive";
    public static final String SOCKET_EVENT_SERVER_READ = "stateRead";
    public static final String SOCKET_EVENT_ADMIN_OFFLINE = "adminOffLine";

    public static final String SOCKET_EVENT_STATE_AWAY = "stateAway";
    public static final String EVENT_UN_AUTHORIZED = "unauthorized";

    //Get Chat Cons
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGING = "paging";
    public static final String MESSAGES = "messages";
    public static final String M_MESSAGE = "Message";
    public static final String TO = "TO";
    public static final String FROM = "From";
    public static final String TO_USER_ID = "1";
    public static final String MESSAGE_ID = "messageId";


    public static final String RECIEVER_ACTION_NAME = "com.devicebee.socketIntent";
    public static final String ID = "id";


    public static final String DURATION = "duration";
    public static final String STATUS = "status";
    public static final String CREATED = "created";

    // Glide Image Res Cons
    public static final int LOW_WIDTH = 100;
    public static final int LOW_HEIGHT = 100;
    public static final int MIDIEUM_WIDTH = 200;
    public static final int MIDIEUM_HEIGHT = 200;
    public static final int MIDIEUM_WIDTH_150 = 150;
    public static final int MIDIEUM_HEIGHT_150 = 150;
    public static final int HIGH_WIDTH = 400;
    public static final int HIGH_HEIGHT = 400;
    public static final int VERY_HIGH_WIDTH = 600;
    public static final int VERY_HIGH_HEIGHT = 600;

    public static final String NET_CONNECTION_LOST = Application.getAppInstance().getString(R.string.opps_no_internet);
    public static final String INTERNET_CONNECTION_TEXT = "Net Connection Lost";

    // Message Type Const
    public static int MESSAGE_UPDATE = 1;
    public static int RECIEVE_NEW_MESSAG = 2;
    public static int ADMIN_TYPING = 3;

    //Admin Status Const
    public static int ADMIN_ONLINE = 4;
    public static int ADMIN_OFFLINE = 5;
    public static int ADMIN_READ_MSG = 8;
    public static int ADMIN_DELEVERY_MSG = 8;


    // Update device token Const
    public static String PUSH_MODE = "pushMode";
    public static String WEB_LINK = "link";
    public static String FB_LINK = "fb_link";
    public static String TWITTER_LINK = "twitter_link";
    public static String INSTAGRAM_LINK = "instagram_link";

    public static String TERM_CONDITION_URL = "http://188.226.178.195/tryAndApply/terms.html";
    public static String TERM_CONDITION_URL_AR = "http://188.226.178.195/tryAndApply/terms_ar.html";
    public static String TERM_CONDITION_URL_RU = "http://188.226.178.195/tryAndApply/terms_ru.html";
    public static String FB_URL = "https://www.facebook.com/tryandapply/";
    public static String TWITTER_URL = "";
    public static String INSTAGRAM_URL = "https://www.instagram.com/tryandapply/";
    public static String SITE_URL = "www.tryandapply.com";
    public static String COMPANY_EMAIL = "info@tryandapply.com";
    public static String COMPANY_SUBJECT = "Try and Apply:";
    public static String COMPANY_NUMBER = "00962777111778";
    public static String ON = "1";
    public static String OFF = "0";
    public static String AUTHENTICATION_ERROR = "Authentication error";
    public static String PRICE = "price";

    public static final String TRANSPORT = "Transport";
    public static final String HOSPITALS = "Hospitals";
    public static final String CINEMAS = "Cinemas";
    public static final String AIRPORTS = "Airports";
    public static final String HOTELS = "Hotels";
    public static final String BANKS = "Banks";
    public static final String MALLS = "Shopping Centres";

    public static final String EVENT_LOGIN = "Login Screen";
    public static final String EVENT_SIGNUP = "Signup Screen";
    public static final String EVENT_VERIFICATION = "Verification Screen";
    public static final String EVENT_DASHBOARD = "Dashboard/Home Screen";
    public static final String EVENT_HELP = "Help Screen";
    public static final String EVENT_UNIVERSITY_MEDIA_DETAILS = "University Media Details Screen";
    public static final String EVENT_LOGOUT = "Logout Event";
    public static final String EVENT_SEARCH_SCREEN = "Search Screen";
    public static final String EVENT_CONTACT_US_SCREEN = "Contact Us Screen";
    public static final String EVENT_BUNDLE_SCREEN = "Bundles Screen";
    public static final String EVENT_ELITE_BUNDLE_SCREEN = "Elite Bundles Screen";
    public static final String EVENT_GOLD_BUNDLE_SCREEN = "Gold Bundles Screen";
    public static final String EVENT_SILVER_BUNDLE_SCREEN = "Silver Bundles Screen";
    public static final String EVENT_FREE_BUNDLE_SCREEN = "Free Bundles Screen";
    public static final String EVENT_RECORDING_SCREEN = "Recording Event";
    public static final String EVENT_LIVE_SCREEN = "Live Screen";
    public static final String EVENT_CHANGE_LANGUAGE_SCREEN = "Change Language Screen";
    public static final String EVENT_ABOUT_TRY_APPY_SCREEN = "About Us Screen";
    public static final String EVENT_APPLY_SCREEN = "Apply Screen";
    public static final String EVENT_LEVEL_OF_STUDY_SCREEN = "Level of Study Screen";
    public static final String EVENT_STREAMS_SELECT_SCREEN = "Stream Select Screen";
    public static final String EVENT_COUNTRY_SELECT_SCREEN = "Country Select Screen";
    public static final String EVENT_FEESS_SELECT_SCREEN = "Fees Select Screen";
    public static final String EVENT_UNIVERSITY_LIST_SCREEN = "University List Screen";
    public static final String EVENT_UPLOAD_DOCUMENT_SCREEN = "Upload Document Screen";
    public static final String EVENT_DOCUMENT_LISTING_SCREEN = "Document list Screen";
    public static final String EVENT_UNIVERSITY_MEDIA_SCREEN = "University List Screen";
    public static final String EVENT_SELECT_UNIVERSITY_SCREEN = "Select University Screen";
    public static final String EVENT_UNIVERSITY_DETAILS_SCREEN = "University Details Screen";
    public static final String EVENT_CHECK_STA_SCREEN = "Check Status Screen";
    public static final String EVENT_UNIVERSITY_INFORMATION = "University Information Screen";
    public static final String EVENT_UNIVERSITY_TUTION_FEES = "University tuition Screen";
    public static final String EVENT_UNIVERSITY_ENTRY_REQUIRMENTS = "University Entry Requirements Screen";
    public static final String EVENT_UNIVERSITY_POI = "University Point Of Interest Screen";
    /*public static final String EVENT_SEARCH_GYMS_SCREEB="Search Gyms";
    public static final String EVENT_SEARCH_RESTAURANT_SCREEN="Search Restaurants";
    public static final String EVENT_SEARCH_TRANSPORT_SCREEN="Search Transport";
    public static final String EVENT_SEARCH_HOSPITAL_SCREEN="Search Hospitals";
    public static final String EVENT_SEARCH_CINEMAS_SCREEB="Search Cinemas";
    public static final String EVENT_SEARCH_AIRPORT_SCREEN="Search Airport";
    public static final String EVENT_SEARCH_HOTELS_SCREEN="Search Hotels";
    public static final String EVENT_SEARCH_SHOPPING_CENTER_SCREEN="Search Shopping Centers";*/
//    public static final String EVENT_UPLOAD_DOCUMENT_SCREEN="Upload Document Screen";
    public static final String EVENT_UPLOAD_DOCUMENT_FROM_GALLARY = "Upload Document from Gallery";
    public static final String EVENT_UPLOAD_DOCUMENT_FROM_SCAN = "Upload Document from Scan";
    public static final String EVENT_SUMMARY_SCREEN = "Summary Screen";
    public static final String EVENT_IMANE_SCREEN = "Image Screen";
    public static final String EVENT_BLOG_SCREEN = "Blog Screen";
    public static final String EVENT_FORGOT_PASSWORD = "Forgot Password Screen";
    public static final String EVENT_CHAT_SCREEN = "Chat Screen";
    public static final String EVENT_CHECK_STATUS_SCREEN = "Check Status Screen";
    public static final String EVENT_APPLICATION_STATUS_DETAILS_SCREEN = "Application Status Details Screen";
    public static final String EVENT_PHONE_NUMBER_SCREEN = "Phone Number Screen";
    public static final String EVENT_OTP_SCREEN = "OTP Screen";

    public static final String EVENT_CHANGE_PASSWORD_SCREEN = "Change Password Screen";
    public static final String EVENT_EDIT_PROFILE_SCREEN = "Edit Profile Screen";
    public static final String EVENT_VIEW_PROFILE_SCREEN = "View Profile Screen";

    public static final String PARAM_USER = "user";
    public static final String PARAM_USER_NAME = "username";
    public static final String PARAM_EMAIL = "user_email";

    public static final String FB_ID = "fbId";


    public static final String COUNTRY_ID_FREE = "country_id";
    public static final String COUNTRY_NAME_FREE = "country_name";
    public static final String UNIVERSITIES_ID_FREE = "universities_id";
    public static final String UNIVERSITIES_NAME_FREE = "universities_name";
    public static final String DOCUMENT_FREE = "document";
    public static final String DOCUMENT_TYPE_FREE = "doc_types";
    public static final String COMMENT_FREE = "comment";


}
