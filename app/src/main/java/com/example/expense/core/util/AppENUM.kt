package com.example.expense.core.util


class AppENUM {
    companion object {
        const val GomecExecutiveProvider = "gomechanic.executive.provider"
        const val DATA = "data"
        const val CAMERA_REQUEST = 123
        const val GALLERY_REQUEST = 124
        val carDocuments =
            arrayOf("RC", "PUC", "Insurance", "Road Tax", "Passenger Tax", "No Document")
        const val NO_DOCUMENT = "No Document"
        const val TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val DETECTING_CALLS = "detecting_calls"
        var battryBrands =
            arrayOf("Exide", "Amaron", "SF-Sonic", "Tata Green", "Ac-Delco", "Varta", "Other")
        var battryBrandsMalaysia = arrayOf("Amaron", "Century", "Hella", "Yuasa", "Varta", "Other")
        var inventoryItemsInsurance = arrayOf(
            "Perfume",
            "Idol",
            "Fog Lamp",
            "USB",
            "Lighter",
            "Speaker",
            "Jack Set",
            "Chargers",
            "Tool Kit",
            "Spare Wheel",
            "Woofers"
        )

        //val timeSlotList: Array<String> = arrayOf("10 AM to 11 AM", "11 AM to 12 PM", "12 PM to 01 PM", "01 PM to 02 PM", "02 PM to 03 PM", "03 PM to 04 PM", "04 PM to 05 PM", "05 PM to 06 PM", "06 PM to 07 PM", "07 PM to 08 PM", "08 PM to 09 PM", "09 PM to 10 PM", "10 PM to 11 PM", "10 AM to 12 PM", "12 PM to 02 PM", "02 PM to 04 PM", "04 PM to 06 PM")
        //val timeSlotPost = arrayOf("10:30:00", "11:30:00", "12:30:00", "13:30:00", "14:30:00", "15:30:00", "16:30:00", "17:30:00", "18:30:00", "19:30:00", "20:30:00", "21:30:00", "22:30:00", "10:00:00", "12:00:00", "14:00:00", "16:00:00")
        val timeSlotList: Array<String> = arrayOf(
            "10 AM to 11 AM",
            "11 AM to 12 PM",
            "12 PM to 01 PM",
            "01 PM to 02 PM",
            "02 PM to 03 PM",
            "03 PM to 04 PM",
            "04 PM to 05 PM",
            "05 PM to 06 PM",
            "06 PM to 07 PM",
            "07 PM to 08 PM",
            "08 PM to 09 PM",
            "09 PM to 10 PM",
            "10 PM to 11 PM"
        )
        val timeSlotPost = arrayOf(
            "10:00:00",
            "11:00:00",
            "12:00:00",
            "13:00:00",
            "14:00:00",
            "15:00:00",
            "16:00:00",
            "17:00:00",
            "18:00:00",
            "19:00:00",
            "20:00:00",
            "21:00:00",
            "22:00:00"
        )
        val timeSlotPostHalf = arrayOf(
            "10:30:00",
            "11:30:00",
            "12:30:00",
            "13:30:00",
            "14:30:00",
            "15:30:00",
            "16:30:00",
            "17:30:00",
            "18:30:00",
            "19:30:00",
            "20:30:00",
            "21:30:00",
            "22:30:00"
        )
        val timeSlotShowHalf = arrayOf(
            "07:00 AM",
            "07:30 AM",
            "08:00 AM",
            "08:30 AM",
            "09:00 AM",
            "09:30 AM",
            "10:00 AM",
            "10:30 AM",
            "11:00 AM",
            "11:30 AM",
            "12:00 AM",
            "12:30 AM",
            "13:00 PM",
            "13:30 PM",
            "14:00 PM",
            "14:30 PM",
            "15:00 PM",
            "15:30 PM",
            "16:00 PM",
            "16:30 PM",
            "17:00 PM",
            "17:30 PM",
            "18:00 PM",
            "18:30 PM",
            "19:00 PM",
            "19:30 PM",
            "20:00 PM",
            "20:30 PM",
            "21:00 PM",
            "21:30 PM",
            "22:00 PM",
            "22:30 PM",
            "23:00 PM"
        )
        val timeSlotShowPostHalf = arrayOf(
            "07:00",
            "07:30",
            "08:00",
            "08:30",
            "09:00",
            "09:30",
            "10:00",
            "10:30",
            "11:00",
            "11:30",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:00",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
            "18:00",
            "18:30",
            "19:00",
            "19:30",
            "20:00",
            "20:30",
            "21:00",
            "21:30",
            "22:00",
            "22:30",
            "23:00"
        )

        val newTimeSlotList: Array<String> = arrayOf(
            "10 AM to 11 AM",
            "11 AM to 12 PM",
            "12 PM to 01 PM",
            "01 PM to 02 PM",
            "02 PM to 03 PM",
            "03 PM to 04 PM",
            "04 PM to 05 PM",
            "05 PM to 06 PM",
            "06 PM to 07 PM",
            "07 PM to 08 PM",
            "08 PM to 09 PM",
            "09 PM to 10 PM",
            "10 PM to 11 PM"
        )
        val newTimeSlotPost = arrayOf(
            "10:00:00",
            "11:00:00",
            "12:00:00",
            "13:00:00",
            "14:00:00",
            "15:00:00",
            "16:00:00",
            "17:00:00",
            "18:00:00",
            "19:00:00",
            "20:00:00",
            "21:00:00",
            "22:00:00"
        )

        const val AUTHORIZATION = "Authorization"
        const val CRAPP = "CRAPP"
        const val FLEET_ID = "fleet_id"
        const val USER_CAR_ID = "user_car_id"
        const val ORDER_ID = "order_id"
        const val GARAGE_ESCALATE_REMARK = "garage_escalate_remark"
        const val DELIVERY_DATE = "delivery_date"
        const val ESTIMATE_FLAG = "estimate_flag"
        const val IS_PDF = "is_pdf"
        const val ORDER_MODEL = "order_model"
        const val POSITION = "position"
        const val PACKAGE = "package"
        const val SERVICE = "service"
        const val CAR_ID = "car_id"
        const val ORDER_TYPE = "order_type"
        const val LEAD_TYPE = "lead_type"
        const val PACKAGE_CAR_ID = "package_car_id"
        const val JOB_CARD = "job_card"
        const val ESTIMATE = "estimate"
        const val SERVICE_NAME = "service_name"
        const val GARAGE_ID = "garage_id"
        const val TXN_IDS = "txn_ids"
        const val FLEET_DEAL_ID = "fleet_deal_id"
        const val GARAGETYPE = "Operation/Garage"
        const val ADMIN = "admin"
        const val OWNER = "owner"
        const val ORDER_OF_GARAGE = "Garage"
        const val ORDER_OF_GOMECHANIC = "Gomechanic"
        const val TYPE_1 = "1"
        const val TYPE_0 = "0"
        const val PICKUP = 1
        const val WALKIN = 2
        const val NAME = "name"
        const val PHONE_POST = "phone"
        const val LEAD_ID_POST = "lead_id"
        const val FLEET_TYPE_POST = "fleet_type"
        const val COUPON_TYPE_POST = "coupon_code_id"
        const val ODOMETER_POST = "odometer_reading"
        const val CR_POST = "cr_id"
        const val GARAGE_POST = "garage_id"
        const val ARRIVAL_MODE_POST = "arrival_mode"
        const val USER_DETAIL = "user_detail"
        const val MOBILE = "mobile"
        const val OTP = "otp"
        const val VERSION = "version"
        const val USER_TYPE = "user_type"
        const val EMAIL = "email"
        const val CHANNEL = "channel"
        const val LOCATION_ID = "location_id"
        const val APP_NAME = "app_name"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val INSURANCE_ORDER_TYPE = 103
        const val RETAIL_ORDER_TYPE = 101
        const val PREVENTIVE_ORDER_TYPE = 100
        const val SOS_ORDER_TYPE = 107
        const val ACCESSORIES_TYPE = 109

        // Inventory parameter
        const val FLEET_ID_POST = "fleet_id"
        const val CAR_ID_POST = "car_id"
        const val ORDER_ID_POST = "order_id"
        const val INVENTORY_ITEMS_POST = "inventory_items"
        const val BATTERY_INFO_POST = "battery_info"
        const val OTHERS_POST = "others"
        const val ID_POST = "id"
        const val ID = "id"
        const val IS_WORKSHOP = "is_workshop"
        const val INITIAL_PROBLEM_POST = "initial_problem"
        const val FUEL_METER_READING_POST = "fuel_meter_reading"
        const val INVENTORY_DOCUMENTS_POST = "inventory_documents"
        const val INVENTORY_COUNT_POST = "inventory_count"
        const val FILE_IMAGES = "insuranceFile"
        const val MASK_IMAGE = "mask_image"
        const val ADDRESS_POST = "address"
        const val RECON = "recon"
        const val PAGE = "page"
        const val WORK_SHOP_ID = "workshop_id"
        const val EPOCH = "epoch"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val LEAD_MODEL = "lead_model"
        const val DRIVER_MODEL = "driver_model"
        const val GARAGE_GST_POST = "garage_gst_status"
        const val PICK_DATE_POST = "pick_date"
        const val TIME_SLOT_POST = "time_slot"
        const val IS_IGST = "is_igst"
        const val EXPRESS_PRIORITTY = "1"
        const val OLD_PHOTO_ADAPTER = 1
        const val NEW_PHOTO_ADAPTER = 2
        const val CLUSTER_PHOTO_ADAPTER = 3
        const val PAYMENT_PHOTO_ADAPTER = 4
        const val DELIVERY_PHOTO_ADAPTER = 5
        const val PARTS_PHOTO_ADAPTER = 6
        const val STICKER_PHOTO_ADAPTER = 7

        //VOICE RECORDING
        //TIME LIST FRAGMENT
        const val DELIVERY_TIME = "delivery_time"
        const val ORDER_PRIORITY = "order_priority"
        const val PICKUP_TIME = "pickup_time"
        const val INSURANCE_TYPE_ORDER = "insurance_order_type"
        const val COMPANY_ID = "company_id"

        ///Order Status
        const val GENERAL = "general"

        // Inventory parameter
        const val DEFAULT_LEADS = "14"
        const val IS_GOMECHANIC = "is_gomechanic"
        const val HST_ORDER_ACT_ID = "hstOrderActId"
        const val CONTRACT_TYPE = "contract_type"
        const val REPLACED = "replaced"
        const val TOPUP = "Top-up"
        const val INSURANCE = "insurance"
        const val IMAGE_URL = "https://dev.eapp.gomechanic.app/"
        const val FLEET_TYPE = "fleet_type"
        const val FILE_ID = "file_id"
        const val IS_MULTIPLE = "is_multiple"

        const val START_DATE_PICKER = "START_DATE_PICKER"
        const val END_DATE_PICKER = "END_DATE_PICKER"

        const val PAID_TAB = 1
        const val PAYMENT_DUE_TAB = 2
        const val APPROVED_TAB = 3
        const val ESCALATED_TAB = 4
        const val REQUESTED_GOM_TAB = 5
        const val SETTLEMENTS_PAYMENT_SUMMARY_TAB = 6
        const val SETTLED_BILLS_TAB = 7

        const val CAR_BASED_ORDERS_TAB = 1
        const val BULK_ORDERS_TAB = 2

        const val CAR_TYPE_DATA = "car_type"
        const val DUPLICATE_ID = "21"


        const val DDMMYYYY = "dd-MM-yyyy"
        const val LIMIT = "limit"
        const val OFFSET = "offset"
        const val STATUS_IDS = "status_ids"
        const val SEARCH_STRING = "search_string"
        const val SINGLE_IMAGE = "single_image"
        const val SEARCH_REG = "search_reg"
        const val FOREGROUND_SERVICE_NOTI_ID = 1111

        //SharedPrefrence
        const val B2B_ORDER = "0"
        const val PREPAID = 1
        const val GENDER = "gender"

        //Health Card
        const val HEALTH_CARD_MODEL = "Health_card_model"
        const val MALFUNCTION_SUB_SERVICE_POSITION = 1
        const val DENT_PENT_SUB_SERVICE_POSITION = 7
        const val leftSwipe = 2
        const val isColor = "1"
        const val isNotColor = "0"
        const val ELECTRICAL_EQUIPMENT_MALFUNCTION_CHECK = "7_2"
        const val OTHERS_TO_BE_ENTERED_MANUALLY_CHECK = "OTHERS_TO_BE_ENTERED_MANUALLY_CHECK"
        const val ELECTRICAL_EQUIPMENT_MALFUNCTION =
            "Electrical Equipment Malfunction (To be entered Manually)"

        const val UPLOAD_CSV = "upload_csv"
        const val RACK_ITEM = "rack_item"
        const val IS_TRAINING_IMAGE = "is_training_image"

        const val AMOUNT = "amount"
        const val FLAVOUR = "flavour"
        const val DISPLAY_NAME = "display_name"
        const val PDF_FILE = "pdf_path"
        const val CAR_SEARCH_TYPE = "CAR_SEARCH_TYPE"
        const val TYPE="type"
        const val MONTH="month"
        const val YEAR="year"
        const val FILE_TYPE="file_type"
        const val FILE_PATH="file_path"
        const val FILE_REF="file_ref"
        const val EXCEL_MIME = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        const val LEAD_ORDER_TYPE = "LEAD_ORDER_TYPE"
        const val SOURCE_ID = "source_id"
        const val OPEN = "open"
        val ACCESSORIES_TYPE_NAME: String = "109"
        const val ALL_PARTS_AND_SERVICE = "all_parts_and_service"
        const val PACKAGE_CART_TEXT = "package_cart_text"
        const val ALL_PACKAGE_AND_SERVICE = "all_package"
        const val PACKAGE_PART_SERVICE = "PACKAGE_PART_SERVICE"
        const val PACKAGE_PRICE = "package_price"
        const val PACKAGE_NAME = "package_name"
        const val PACKAGE_CATEGORY = "package_category"
        const val PACKAGE_IMAGE = "package_image"
        const val PACKAGE_COUPOUN_CODE = "package_coupoun_code"
        const val TYRE_MODEL = "tyre_model"
        const val IS_TYRE_MODEL = "is_tyre_model"
        const val EXTRA_PRICE = "extra_price"
        const val PACKAGE_ID = "package_id"
        const val DEAL_ID = "deal_id"
        const val WORK_DONE = "work_done"
        const val GM_NUM = "gm_num"
        //WhatsApp Package
        const val WHATS_APP_PACKAGE = "com.whatsapp"
        const val WHATS_APP_BUSNINESS_PACKAGE = "com.whatsapp.w4b"
        const val LOCATION_REQUEST = 1234
        const val IS_SPRARES_CAR = "is_spares_car"
        const val SERVER = "server"
    }


    abstract class UserKeySaveENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val USER_TYPE = "user_type"
            const val WORKSHOP_USER_ID = "workshop_user_id"
            const val USER_NAME = "user_name"
            const val WORKSHOP_NAME = "workshop_name"
            const val IS_RETAILER_SELECTED = "is_retailer_selected"
            const val DISTRIBUTOR_NAME = "distributor_name"
            const val WORKSHOP_MOBILE = "workshop_mobile"
            const val WORKSHOP_ID = "workshop_id"
            const val DISTRIBUTOR_ID = "distributor_id"
            const val WORKSHOP_LOCATION = "location"
            const val DISTRIBUTOR_LOCATION = "distributor_location"
            const val GARAGE_ID = "GARAGE_ID"
            const val IS_POS_AVAILABLE = "is_pos_available"
            const val IS_ADMIN = "is_admin"
            const val WORKSHOP_ADDRESS = "workshop_address"
            const val DRIVER_ID = "driver_id" //This is the userid and not the actual driverid
            const val REAL_DRIVER_ID = "real_driver_id"
            const val DRIVER_NAME = "driver_name"

            const val ADVISER_ID = "adviser_id"
            const val ADVISER_NAME = "adviser_name"
            const val ROLES_FOUND = "roles_found"
            const val USER_ROLE = "user_role"
            const val PROFILE_IMAGE = "profile_image"
            const val LICENSE_NUMBER = "license_number"
            const val AADHAR_NUMBER = "aadhar_number"
            const val DRIVER_PROFILE_ID = "driver_profile_id"
            const val GST_NUM = "gst_num"


            const val OWNER_ID = "owner_id"
            const val LOGIN_MOBILE = "login_mobile"
            const val RETAILER_MOBIL = "retailer_mobil"
            const val SHOW_SPARES = "show_spares"
            const val SHOW_PREVIOUS_PAYMENT = "show_previous_payment"
            const val NEW_PAYMENT_LOGIC_YEAR = "new_payment_logic_year"
            const val NEW_PAYMENT_LOGIC_MONTH = "new_payment_logic_month"

        }
    }

    abstract class AddLeadKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val VALUE_INSURANCE = "Insurance"
            const val VALUE_accessories = "Accessories Retail"
            const val EVENT_TYPE = "EVENT_TYPE"
        }
    }

    abstract class InventoryAdapterENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            var BATTTERY = 1
            var INVETORY = 2
            var CAR_DOC = 3
        }
    }

    abstract class OrderTypeENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            var RETAIL = "retail"
            var INSURANCE = "insurance"
            var B2B = "b2b"
            var INSURANCE_STR = "103"
            var ALL = "all"
            var RETAIL_FIRST_CAPS = "Retail"
            var INSURANCE_FIRST_CAPS = "Insurance"
            var B2B_FIRST_CAPS = "B2B"
        }
    }

    abstract class NPSTypeENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            var ALL = "all"
        }
    }

    abstract class OrderDetailPage private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            var ORDER_SUMMARY = "Order Summary"
            var ORDER_DETAIL = "Order Details"
            var ORDER_INVENTORY = "Inventory"
            var ORDER_JOB_CARD = "Job Card"
            var ORDER_ESTIMATE = "Order Estimate"
            var ORDER_PAYMENT = "Payments"
            var ORDER_HISTORY = "Order History"
            var ORDER_FILES = "Files"
            var ORDER_SURVEYOR_BILL = "Surveyor Bill"
            var ORDER_CUSTOMER_BILL = "Customer Bill"
            var SPARES = "Spares"
            var HEALTH_CARD = "Health Card"
            var allPages = arrayListOf<String>(
                "Order Summary", "Order Details", "Inventory",
                "Job Card", "Order Estimate", "Payments", "Order History", "Files", "Surveyor Bill",
                "Customer Bill", "Spares", "Health Card"
            )
        }
    }

    abstract class OrderPaymentStatus private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val SUCCESSFUL = 300
            const val INITIATED = 280
            const val OPENED = 285
            const val CANCELLED = 290
            const val TEMPERED = 325
            const val UNSUCCESSFUL = 350
            const val CASH = 3
            const val CHEQUE = 4
            const val PAYTM = 2
            const val PAYTM_GATEWAY = 1
            const val PAYUMONEY_GATEWAY = 5
            const val INSTAMOJO_GATEWAY = 6
            const val CARD = 7
            const val MOBI = 8
            const val GOAPPMONEY = 10
            const val DO_RECEIVED = 9
            const val RAZORPAY = 12
            const val GOMECHANIC = 1
            const val GARAGE = 2
            const val GOOGLE_PAY = 11
            const val POS = 14
            const val NEFT = 13
            const val REFUND_INITIATED_TRANSACTION_STATUS = 400
            const val REFUND_TRANSACTION_STATUS = 450
        }
    }

    abstract class OrderFeedbackQuestionTypes private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val HEADLIGHT_WORKING: Int = 1
            const val CAR_STARTING: Int = 2
            const val WINDOW_LOCKS_WORKING: Int = 3
            const val INVENTORY_IN_PLACE: Int = 4
            const val INTERIOR_CLEAN: Int = 5
            const val FUEL_FINE: Int = 6
            const val CLUTCH_BRAKES_WORKING: Int = 7
            const val HANDBRAKE_WORKING: Int = 8
        }
    }


    abstract class OrderDetailKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val BILL_AMT = "billAmount"
            const val TOTAL_PAID = "totalPaid"
            const val TOTAL_DUE = "totalDue"
            const val PAYMENT_MODEL = "paymentModel"
            const val ORDER_MODEL = "orderModel"
            const val POSITION = "position"
            const val DIRECTION = "direction"
            const val COUPONS = "coupons"
            const val COUPON_CODE_ID = "coupon_code_id"
            const val STATUS_ID = "status_id"
            const val ORDER_ID = "order_id"
            const val ORDER_TYPE = "order_type"
            const val GM_NUM = "gm_num"
            const val CPM_ID = "cpm_id"
            const val TITLE = "title"
            const val GARAGE_ID = "garage_id"
            const val DEAL_ID = "deal_id"
            const val VERSION = "version"
            const val IS_SUBS = "is_subs"
            const val GARAGE_GST_STATUS = "garage_gst_status"
            const val CAR_ID = "car_id"
            const val FLEET_UPDATE = "fleet_update"
            const val MODEL_ID = "model_id"
            const val REMARK_TEXT = "remark_text"
            const val COUPON_ID = "coupon_id"
            const val PAYMENT_REMARK = "payment_remark"
            const val EVENT_BUS_MSG = "event_bus_msg"
            const val CAT_ID = "cat_id"
            const val SUB_CAT_ID = "sub_cat_id"

            const val GOMEC_ESTIMATE_MODEL = "gomec_estimate_model"

            const val GM_TRANSACTION_ID = "gm_transaction_id"

            const val FLEET_ID = "fleet_id"
            const val USER_CAR_ID = "user_car_id"
            const val ONLY_VERSION = "version"
            const val FETCH_RETAIL_DATA = "fetch_retail_data"
            const val DEFAULT_WORK_DONE_ID = "default_work_done_id"
        }
    }

    abstract class PaymentStaticValue private constructor() {
        companion object {

            val GARAGE_ORDER_OF = "2"
            var CASH = "Cash"
            var CHEQUE = "Cheque"
            var DEBIT_CREDIT_CARD = "Debit/Credit Card"
            var ONLINE_PAYTM = "Online_Paytm"
            var PAYTM = "Paytm"
            var ONLINE_PAY_U_MONEY = "Online_PayUMoney"
            var ONLINE_INSTAMOJO = "Online_Instamojo"
            var D0_RECEIVED = "Garage D.O."
            var BANK_NAME = "bank_name"
            var TRANSACTION_ID = "transaction_id"
            var TYPE = "type"
            var GM_TRANSACTION_ID = "gm_transaction_id"
            var TRANSACTION_STATUS = "transaction_status"
            var CHANNEL = "channel"
            var COLLECTION_BY = "collected_by"
            var TRANSACTION_AMOUNT = "transaction_amount"
            var GOOGLE_PAY = "Google Pay"
            var POS = "POS"
            var NEFT = "NEFT / RTGS"

            var PAYMENT_MODE = arrayOf(
                "Select Type",
                "Cash",
                "PayTM",
                "Google Pay",
                "Cheque",
                "Debit/Credit Card"
            )
            var INSURANCE_PAYMENT_MODE = arrayOf(
                "Select Type",
                "Cash",
                "PayTM",
                "Google Pay",
                "Cheque",
                "Debit/Credit Card",
                "Garage D.O."
            )
            var BANK_LIST = arrayOf(
                "Select Bank",
                "ABHYUDAYA CO-OP BANK LTD",
                "ABU DHABI COMMERCIAL BANK",
                "AKOLA DISTRICT CENTRAL CO-OPERATIVE BANK",
                "AKOLA JANATA COMMERCIAL COOPERATIVE BANK",
                "ALLAHABAD BANK",
                "ALMORA URBAN CO-OPERATIVE BANK LTD.",
                "ANDHRA BANK",
                "ANDHRA PRAGATHI GRAMEENA BANK",
                "APNA SAHAKARI BANK LTD",
                "AUSTRALIA AND NEW ZEALAND BANKING GROUP LIMITED.",
                "AXIS BANK",
                "BANK INTERNASIONAL INDONESIA",
                "BANK OF AMERICA",
                "BANK OF BAHRAIN AND KUWAIT",
                "BANK OF BARODA",
                "BANK OF CEYLON",
                "BANK OF INDIA",
                "BANK OF MAHARASHTRA",
                "BANK OF TOKYO-MITSUBISHI UFJ LTD.",
                "BARCLAYS BANK PLC",
                "BASSEIN CATHOLIC CO-OP BANK LTD",
                "BHARATIYA MAHILA BANK LIMITED",
                "BNP PARIBAS",
                "CALYON BANK",
                "CANARA BANK",
                "CAPITAL LOCAL AREA BANK LTD.",
                "CATHOLIC SYRIAN BANK LTD.",
                "CENTRAL BANK OF INDIA",
                "CHINATRUST COMMERCIAL BANK",
                "CITIBANK NA",
                "CITIZENCREDIT CO-OPERATIVE BANK LTD",
                "CITY UNION BANK LTD",
                "COMMONWEALTH BANK OF AUSTRALIA",
                "CORPORATION BANK",
                "CREDIT SUISSE AG",
                "DBS BANK LTD",
                "DENA BANK",
                "DEUTSCHE BANK",
                "DEUTSCHE SECURITIES INDIA PRIVATE LIMITED",
                "DEVELOPMENT CREDIT BANK LIMITED",
                "DHANLAXMI BANK LTD",
                "DICGC",
                "DOMBIVLI NAGARI SAHAKARI BANK LIMITED",
                "FIRSTRAND BANK LIMITED",
                "GOPINATH PATIL PARSIK JANATA SAHAKARI BANK LTD",
                "GURGAON GRAMIN BANK",
                "HDFC BANK LTD",
                "HSBC",
                "ICICI BANK LTD",
                "IDBI BANK LTD",
                "IDRBT",
                "INDIAN BANK",
                "INDIAN OVERSEAS BANK",
                "INDUSIND BANK LTD",
                "INDUSTRIAL AND COMMERCIAL BANK OF CHINA LIMITED",
                "ING VYSYA BANK LTD",
                "JALGAON JANATA SAHKARI BANK LTD",
                "JANAKALYAN SAHAKARI BANK LTD",
                "JANASEVA SAHAKARI BANK (BORIVLI) LTD",
                "JANASEVA SAHAKARI BANK LTD. PUNE",
                "JANATA SAHAKARI BANK LTD (PUNE)",
                "JPMORGAN CHASE BANK N.A",
                "KALLAPPANNA AWADE ICH JANATA S BANK",
                "KAPOL CO OP BANK",
                "KARNATAKA BANK LTD",
                "KARNATAKA VIKAS GRAMEENA BANK",
                "KARUR VYSYA BANK",
                "KOTAK MAHINDRA BANK",
                "KURMANCHAL NAGAR SAHKARI BANK LTD",
                "MAHANAGAR CO-OP BANK LTD",
                "MAHARASHTRA STATE CO OPERATIVE BANK",
                "MASHREQBANK PSC",
                "MIZUHO CORPORATE BANK LTD",
                "MUMBAI DISTRICT CENTRAL CO-OP. BANK LTD.",
                "NAGPUR NAGRIK SAHAKARI BANK LTD",
                "NATIONAL AUSTRALIA BANK",
                "NEW INDIA CO-OPERATIVE BANK LTD.",
                "NKGSB CO-OP BANK LTD",
                "NORTH MALABAR GRAMIN BANK",
                "NUTAN NAGARIK SAHAKARI BANK LTD",
                "OMAN INTERNATIONAL BANK SAOG",
                "ORIENTAL BANK OF COMMERCE",
                "PARSIK JANATA SAHAKARI BANK LTD",
                "PRATHAMA BANK",
                "PRIME CO OPERATIVE BANK LTD",
                "PUNJAB AND MAHARASHTRA CO-OP BANK LTD.",
                "PUNJAB AND SIND BANK",
                "PUNJAB NATIONAL BANK",
                "RABOBANK INTERNATIONAL (CCRB)",
                "RAJGURUNAGAR SAHAKARI BANK LTD.",
                "RAJKOT NAGARIK SAHAKARI BANK LTD",
                "RESERVE BANK OF INDIA",
                "SBERBANK",
                "SHINHAN BANK",
                "SHRI CHHATRAPATI RAJARSHI SHAHU URBAN CO-OP BANK LTD",
                "SOCIETE GENERALE",
                "SOLAPUR JANATA SAHKARI BANK LTD.SOLAPUR",
                "SOUTH INDIAN BANK",
                "STANDARD CHARTERED BANK",
                "STATE BANK OF BIKANER AND JAIPUR",
                "STATE BANK OF HYDERABAD",
                "STATE BANK OF INDIA",
                "STATE BANK OF MAURITIUS LTD",
                "STATE BANK OF MYSORE",
                "STATE BANK OF PATIALA",
                "STATE BANK OF TRAVANCORE",
                "SUMITOMO MITSUI BANKING CORPORATION",
                "SYNDICATE BANK",
                "TAMILNAD MERCANTILE BANK LTD",
                "THANE BHARAT SAHAKARI BANK LTD",
                "THE A.P. MAHESH CO-OP URBAN BANK LTD.",
                "THE AHMEDABAD MERCANTILE CO-OPERATIVE BANK LTD.",
                "THE ANDHRA PRADESH STATE COOP BANK LTD",
                "THE BANK OF NOVA SCOTIA",
                "THE BANK OF RAJASTHAN LTD",
                "THE BHARAT CO-OPERATIVE BANK (MUMBAI) LTD",
                "THE COSMOS CO-OPERATIVE BANK LTD.",
                "THE DELHI STATE COOPERATIVE BANK LTD.",
                "THE FEDERAL BANK LTD",
                "THE GADCHIROLI DISTRICT CENTRAL COOPERATIVE BANK LTD",
                "THE GREATER BOMBAY CO-OP. BANK LTD",
                "THE GUJARAT STATE CO-OPERATIVE BANK LTD",
                "THE JALGAON PEOPLES CO-OP BANK",
                "THE JAMMU AND KASHMIR BANK LTD",
                "THE KALUPUR COMMERCIAL CO. OP. BANK LTD.",
                "THE KALYAN JANATA SAHAKARI BANK LTD.",
                "THE KANGRA CENTRAL CO-OPERATIVE BANK LTD",
                "THE KANGRA COOPERATIVE BANK LTD",
                "THE KARAD URBAN CO-OP BANK LTD",
                "THE KARNATAKA STATE APEX COOP. BANK LTD.",
                "THE LAKSHMI VILAS BANK LTD",
                "THE MEHSANA URBAN COOPERATIVE BANK LTD",
                "THE MUNICIPAL CO OPERATIVE BANK LTD MUMBAI",
                "THE NAINITAL BANK LIMITED",
                "THE NASIK MERCHANTS CO-OP BANK LTD. NASHIK",
                "THE RAJASTHAN STATE COOPERATIVE BANK LTD.",
                "THE RATNAKAR BANK LTD",
                "THE ROYAL BANK OF SCOTLAND N.V",
                "THE SAHEBRAO DESHMUKH CO-OP. BANK LTD.",
                "THE SARASWAT CO-OPERATIVE BANK LTD",
                "THE SEVA VIKAS CO-OPERATIVE BANK LTD (SVB)",
                "THE SHAMRAO VITHAL CO-OPERATIVE BANK LTD",
                "THE SURAT DISTRICT CO OPERATIVE BANK LTD.",
                "THE SURAT PEOPLES CO-OP BANK LTD",
                "THE SUTEX CO.OP. BANK LTD.",
                "THE TAMILNADU STATE APEX COOPERATIVE BANK LIMITED",
                "THE THANE DISTRICT CENTRAL CO-OP BANK LTD",
                "THE THANE JANATA SAHAKARI BANK LTD",
                "THE VARACHHA CO-OP. BANK LTD.",
                "THE VISHWESHWAR SAHAKARI BANK LTD. PUNE",
                "THE WEST BENGAL STATE COOPERATIVE BANK LTD",
                "TJSB SAHAKARI BANK LTD.",
                "TUMKUR GRAIN MERCHANTS COOPERATIVE BANK LTD.",
                "UBS AG",
                "UCO BANK",
                "UNION BANK OF INDIA",
                "UNITED BANK OF INDIA",
                "UNITED OVERSEAS BANK",
                "VASAI VIKAS SAHAKARI BANK LTD.",
                "VIJAYA BANK",
                "WEST BENGAL STATE COOPERATIVE BANK",
                "WESTPAC BANKING CORPORATION",
                "WOORI BANK",
                "YES BANK LTD",
                "ZILA SAHKARI BANK LTD GHAZIABAD"
            )
        }
    }

    abstract class DateUtilKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val LIST_DISPLAY_FORMAT = "dd MMM, hh:mm a"
            const val FORMAT_WITH_TIME = "yyyy-MM-dd HH:mm:ss"
            const val FORMAT_DAY = "MMM dd"
            const val FORMAT_IN_SIMPLE = "yyyy-MM-dd"
            const val FORMAT_UPDATED_DATE = "EEE, dd MMM yy"
            const val FORMAT_SIMPLE_YEAR_FIRST = "yyyy-MM-dd"
            const val FORMAT_SIMPLE_DATE_FIRST = "dd/MM/yyyy"
            const val FORMAT_DAY_REVERSE = "dd MMM"
            const val TIME_FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss"
            const val DATE_SIMPLE_PATTREN_WITH_TIME = "dd-MM-yyyy hh:mm a"
            const val DATE_SIMPLE_PATTREN_WITH_TIME_2 = "dd/MM/yyyy HH:mm:ss"
            const val DATE_SIMPLE_PATTREN_WITH_TIME_3 = "dd/MM/yyyy HH:mm a"
            const val TIME_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss"
            const val TIME_FORMAT_3 = "HH:mm:ss"
            const val TIME_FORMAT_4 = "MMM yyyy"
            const val TIME_FORMAT_5 = "MMM dd, yyyy"
        }
    }

    abstract class FilterKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val GOMECHANIC = "gomechanic"
            const val GARAGE = "garage"
            const val GARAGE_ID = "garage_id"
            const val CAR_TYPE_ID = "car_type_id"
            const val ORDER_OF = "order_of"

            const val COMPLETE = "complete"
            const val VERSION = "version"
            const val ORDER_ID = "order_id"
            const val CR_ID = "cr_id"
        }
    }


    abstract class CreateOrderKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val ORDER_OF = "ORDER_OF"
            const val GOMECHANIC = "gomechanic"
            const val GARAGE = "garage"
            const val TYPE_ONE = 1
            const val TYPE_SEVEN = 7
            const val ORDER_FROM = "ORDER_FROM"
            const val SELECTED_LEAD = "selected_lead"
            const val FLEET = "FLEET"
            const val OFFSET = "offset"
            const val LIMIT = "limit"
            const val VERSION = "version"
            const val ORDER_ID = "order_id"
            const val CAR = "car"
            const val ODOMETER_READING = "odometer_reading"
            const val REG_NUMBER = "registration_number"
            const val CAR_ID = "car_id"
            const val SELECTED_CAR = "SELECTED_CAR"
            const val TYPE = "type"
            const val ORDER_MODEL = "ORDER_MODEL"
            const val NAME = "NAME"
            const val CAR_REG_NO = "registration_no"
        }
    }

    abstract class CommonFilterFragmentKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val FRAGMENT_TYPE = "FRAGMENT_TYPE"
            const val FRAGMENT_CAR_DETAILS = "FRAGMENT_CAR_DETAILS"
            const val FRAGMENT_CAR_VARIANT = "FRAGMENT_CAR_VARIANT"
            const val FRAGMENT_SAVE_ORDER_TIME = "FRAGMENT_SAVE_ORDER_TIME"
            const val FRAGMENT_SAVE_ORDER_INSURANCE_BROKER = "FRAGMENT_SAVE_ORDER_INSURANCE_BROKER"
            const val FRAGMENT_SAVE_ORDER_INSURANCE_ADDRESS =
                "FRAGMENT_SAVE_ORDER_INSURANCE_ADDRESS"
            const val FRAGMENT_SAVE_ORDER_SHOP_ADDRESS = "FRAGMENT_SAVE_ORDER_SHOP_ADDRESS"
            const val FRAGMENT_SAVE_ORDER_INSURANCE_COMPANY =
                "FRAGMENT_SAVE_ORDER_INSURANCE_COMPANY"
            const val FRAGMENT_GARAGE_LIST = "FRAGMENT_GARAGE_LIST"
            const val FRAGMENT_CAR_VARIANTS_BY_MODEL_LIST = "FRAGMENT_CAR_VARIANTS_BY_MODEL_LIST"
            const val PAYMENT_MODES_LIST = "PAYMENT_MODES_LIST"
            const val BANK_NAMES_LIST = "BANK_NAMES_LIST"
            const val INVENTORY_REMARKS_LIST = "INVENTORY_REMARKS_LIST"
            const val WORKSHOPS_LIST = "WORKSHOPS_LIST"
            const val DISTTRIBUTOR_LIST = "DISREIBUTOR_LIST"
            const val RETAILER_LIST = "RETAILER_LIST"
            const val WORKSHOPS_LIST2 = "WORKSHOPS_LIST2"
            const val BRAND_NAME = "BRAND_NAME"
            const val MODEL_NAME = "MODEL_NAME"
            const val FUEL_TYPE = "FUEL_TYPE"
            const val BANK_ACCOUNT_TYPE = "BANK_ACCOUNT_TYPE"
            const val MLY_BANK_ACCOUNT = "MLY_BANK_ACCOUNT"
            const val YEAR_OF_MFG = "YEAR_OF_MFG"
            const val NEW_CAR_VARIANT = "NEW_CAR_VARIANT"
            const val ABS = "ABS"
            const val TRANSMISSION = "TRANSMISSION"
            const val TAX_BRACKETS = "TAX_BRACKETS"
        }
    }

    abstract class InsuranceBillTypeKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val INSURANCE_SURVEYOR_BILL = "INSURANCE_SURVEYOR_BILL"
            const val INSURANCE_VIEW_ONLY = "INSURANCE_VIEW_ONLY"
            const val INSURANCE_VIEW_ONLY_TRUE = 1
        }
    }

    abstract class SparePartStatuses private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val OPEN = 15
            const val IN_PROGRESS = 30
            const val INVOICED = 45
            const val CANCELLED = 60
            const val DISPATCHED = 75
            const val DELIVERED = 90
            const val RETURNED = 105
            const val CANCELLED_BY_GOM = 65
            const val APPROVED = 30
        }
    }

    abstract class HealthCardKey private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val ORDER_ID = "order_id"
            const val HEALTH_CARD_JSON = "health_card_json"
            const val ALL_GREEN = "all_green"
            const val SWIPE_AVOID_KEY = 19
        }
    }

    abstract class MobilOrderStatuses private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val MOBIL_NEW_ORDER = 10
            const val MOBIL_APPROVAL_PENDING_ORDER = 20
            const val MOBIL_APPROVED_ORDER = 30
            const val MOBIL_CANCELLED_ORDER = 40
            const val MOBIL_DELIVERED_ORDER = 50
        }
    }

    abstract class FirebaseEventsENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val LOGIN = "login"
            const val LOGOUT = "logout"
            const val CHANGE_LANGUAGE = "select_language"
            const val BOTTOM_TAB = "bottom_tab"
            const val TAP_SEARCH = "tap_search"
            const val TAP_NOTIFICATION = "tap_notification_center"
            const val TAP_SIDE_MENU = "tap_side_menu"
            const val TAP_SPARE_ANALYSIS_BANNER = "tap_spare_analysis_banner"
            const val TAP_SPARE_ANALYSIS_BANNER_RETAILER = "tap_spare_analysis_banner_retailer"
            const val TAP_MANDATORY_TRAINING = "tap_mandatory_training"
            const val TAP_WALLET = "tap_gomechanic_wallet"
            const val TAP_WALLET_DETAILS = "select_wallet_amount_detail"
            const val TAP_WALLET_DETAILS_RETAILER = "select_wallet_amount_detail_retailer"
            const val TAP_RECON = "tap_recon_amount"
            const val TAP_ORDER_HISTORY = "tap_order_history"
            const val TAP_ORDER_HISTORY_INSURANCE = "tap_order_history_ins"
            const val TAP_WALLET_HISTORY = "tap_wallet_history"
            const val SELECT_SETTLEMENT_TABS = "select_settlement_tabs"
            const val TAP_DOWNLOAD_BILL = "tap_download_bill"
            const val TAP_RETAILER_CALL = "tap_retailer_call"
            const val TAP_CALL = "tap_call"
            const val TAP_WORKSHOP_CALL = "tap_workshop_call"
            const val TAP_DISTRIBUTOR_CALL = "tap_distributor_call"
            const val TAP_RETAILER_MAP = "tap_retailer_map"
            const val TAP_MAP = "tap_map"
            const val TAP_WORKSHOP_MAP = "tap_workshop_map"
            const val TAP_SPARE_BANNER = "tap_spare_banner"
            const val TAP_SPARE_SEE_ALL = "tap_spare_see_all"
            const val TAP_SPARE_CATEGORY = "tap_spare_category"
            const val TAP_DISTRIBUTOR_MAP = "tap_distributor_map"
            const val TAP_RETAILER_INFO = "tap_retailer_info"
            const val TAP_NOTIFICATION_TYPE = "tap_notification_type"
            const val TAP_NOTIFICATION_PANEL = "tap_notification"
            const val TAP_LIVE_ORDERS = "live_orders"
            const val TAP_NEW_ORDERS = "self_order"
            const val TAP_OTP_TRIGGERED_FOR_DROP = "tap_drop_otp"
            const val TAP_VERIFY_OTP_TRIGGERED_FOR_DROP = "tap_verify_drop_otp"
            const val TAP_ASSIGN_TO_SELF = "assign_to_self"
            const val TAP_ORDER_LEAD_CALL_BUTTON = "tap_order_lead_call_button"
            const val FORCE_SHOWN_SETTLEMENTS_SCREEN = "force_shown_settlements_screen"
            const val FORCE_SHOWN_DRIVERS_PENDING_SCREEN = "force_shown_drivers_pending_screen"
            const val PLACE_TOP_SPARE_ORDER = "place_top_spare_order"
            const val TAP_PLACE_TOP_SPARE_ORDER = "tap_place_top_spare_order"
            const val TAP_PENALTY_REWARDS = "tap_penalty_rewards"
            const val TAP_EMPLOYEE_PAGE_TAB = "tap_employee_page_tab"
            const val TAP_LEADERBOARD_BUTTON = "tap_leaderboard_button"
            const val TAP_SEE_ALL_RETAILERS = "tap_see_all_retailers"
            const val TAP_SEE_ALL_WORKSHOPS = "tap_see_all_workshops"
            const val TAP_SEE_ALL_DISTRIBUTOR = "tap_see_all_distributor"
            const val TAP_BANK_DETAIL = "tap_bank_detail"
            const val TAP_CALENDAR = "tap_calendar"
            const val TAP_CONSOLIDATED_INVOICE = "download_consolidated_invoice"
            const val TAP_ALL_INVOICE = "download_all_invoice"
            const val DATE_CHANGE = "date_change"
            const val TAP_APPROVE_SETTLEMENT = "tap_approve_settlement"
            const val TAP_ESCALATE_SETTLEMENT = "tap_escalate_settlement"
            const val TAP_RECON_DETAILS = "tap_recon_details"
            const val TAP_PAYMENT_HISTORY = "tap_payment_history"
            const val TAP_MAKE_PAYMENT = "tap_make_payment"
            const val TAP_REQUEST_PAYMENT = "tap_request_payment"
            const val TAP_SEE_MORE = "tap_see_more"
            const val TAP_HIDE_MORE = "tap_hide_more"
            const val UPLOAD_PAYMENT_SCREENSHOTS = "upload_payment_screenshots"
            const val TAP_COPY_ACC_DETAILS = "tap_copy_acc_details"
            const val TAP_RETAIL_TAB = "tap_retail_tab"
            const val TAP_INSURANCE_TAB = "tap_insurance_tab"
            const val TAP_B2B_TAB = "tap_b2b_tab"
            const val TAP_ARRIVAL_MODE = "tap_arrival_mode"
            const val GEO_FENCE_CREATED = "geo_fence_created"
            const val GEO_FENCE_CREATION_FAILED = "geo_fence_creation_failed"
            const val GEO_FENCE_TRANSITION_EVENT = "geo_fence_transition_event"
            const val GEO_FENCE_EVENT_FAILED = "geo_fence_event_failed"
            const val GEO_FENCE_EVENT_PICKUP_COMPLETE = "geo_fence_event_pickuo_complete"

            const val MOENGAGE_NPS_EVENT = "moengage_nps_event"
            const val MOENGAGE_LEADERBOARD_EVENT = "moengage_leaderboard"
            const val MOENGAGE_SPARE_ANALYSIS_EVENT = "moengage_spare_analysis"
            const val MOENGAGE_RECON_LIMIT_EVENT = "moengage_recon_limit"
            const val MOENGAGE_SETTLEMENTS_PENDING_EVENT = "moengage_settlements_pending"
            const val MOENGAGE_ESCALATIONS_PENDING_EVENT = "moengage_escalations_pending"

            const val TAP_RETAILER_ORDER_HISTORY = "tap_order_history"
            const val TAP_RETAILER_INVENTORY = "tap_retailer_inventory"
            const val TAP_INVENTORY = "tap_inventory"
            const val TAP_CREATE_ORDER = "tap_create_order"
            const val TAP_RESEND_OTP = "tap_resend_otp"
            const val OTP_ORDER_CONFIRM = "otp_order_confirm"
            const val OTP_STATUS = "otp_status"
            const val TAP_ADD_PART = "tap_add_part"
            const val ADD_RETAILER_WORKSHOP = "add_retailer_workshop"
            const val TAP_ADD_PART_WAREHOUSE = "tap_add_part_from_warehouse"
            const val ADD_PART_INVENTORY_ADD_SUCCESS = "add_part_inventory_success"
            const val ADD_PART_INVENTORY_DELETE_SUCCESS = "add_part_inventory_delete_success"
            const val ADD_PART_WAREHOUSE_SUCCESS = "add_part_warehouse_success"
            const val ADD_PART_MOBILE_WAREHOUSE_SUCCESS = "add_part_mobil_warehouse_success"
            const val TAP_ADD_PART_INVENTORY = "tap_add_part_from_inventory"
            const val TAP_RETAILER_CONFIRM_ORDER = "tap_retailer_confirm_order"
            const val TAP_ADD_RACK = "tap_add_rack"
            const val TAP_DELETE_RACK = "tap_delete_rack"
            const val TAP_EDIT_RACK = "tap_edit_rack"
            const val TAP_ADD_RACK_ITEM = "tap_add_rack_item"
            const val TAP_ADD_RACK_ITEM_DELETE = "tap_add_rack_item_delete"
            const val RACK_ITEM_ADDED_SUCCESS = "rack_item_add_success"
            const val ADD_RACK_SUCCESS = "add_rack_success"
            const val RACK_SUCCESSFULLY_CREATED = "rack_successfully_created"
            const val ORDER_HISTORY_INVOICE_DOWNLOAD = "order_history_invoice_download_success"
            const val SELECT_BRAND = "select_brand"
            const val SELECT_MODEL = "select_model"
            const val SELECT_PART = "select_part"
            const val SELECT_WORKSHOP = "select_workshop"
            const val TRAINING_MCQ_SUBMIT = "training_mcq_submit"
            const val TRAINING_MCQ_FAIL = "training_mcq_fail"
            const val TRAINING_MCQ_PASS = "training_mcq_pass"
            const val FORCE_SHOW_TRAINING = "force_show_training"

            const val TAP_SETTLEMENTS_BANNER = "tap_settlements_banner"
            const val TAP_PRIVATE_LABEL_BANNER = "tap_private_label_banner"
            const val TAP_ORDER_PRIVATE_LABEL_BANNER = "tap_order_private_label_banner"
            const val TAP_CURATED_TRAY = "tap_curated_tray"
            const val TAP_SPARES_CREDITS_LEFT_BANNER = "tap_spares_credits_left_banner"
            const val TAP_TRAINING_BANNER = "tap_training_banner"
            const val TAP_NPS_BANNER = "tap_nps_banner"
            const val TAP_LEADERBOARD_BANNER = "tap_leaderboard_banner"
            const val TAP_PRIVATE_LABEL_HISTORY = "TAP_PRIVATE_LABEL_HISTORY"
            const val TAP_PRIVATE_LABEL_TRACKING = "TAP_PRIVATE_LABEL_TRACKING"
            const val TAP_PRIVATE_LABEL = "TAP_PRIVATE_LABEL"
            const val TAP_VIEW_CART = "tap_view_cart"
            const val TAP_ADD_PRIVATE_LABEL = "tap_add_private_label"
            const val TAP_GO_TO_CART_PRIVATE_LABEL= "TAP_GO_TO_CART_PRIVATE_LABEL"
            const val TAP_NOTIFY = "TAP_NOTIFY"
            const val TAP_DELETE_PRIVATE_LABEL = "tap_delete_private_label"
            const val TAP_PRIVATE_LABEL_SEE_MORE = "TAP_PRIVATE_LABEL_SEE_MORE"
            const val TAP_PRIVATE_LABEL_CHECKOUT = "tap_private_label_checkout"
            const val TAP_PRIVATE_LABEL_DOWNLOAD_PDF = "TAP_PRIVATE_LABEL_DOWNLOAD_PDF"
            const val TAP_PRIVATE_LABEL_ORDER_DETAILS = "TAP_PRIVATE_LABEL_ORDER_DETAILS"
            const val TAP_PRIVATE_LABEL_ORDER_INVOICE = "TAP_PRIVATE_LABEL_ORDER_INVOICE"
            const val TAP_PRIVATE_LABEL_REORDER = "TAP_PRIVATE_LABEL_REORDER"
            const val TAP_PRIVATE_LABEL_PURCHASE = "TAP_PRIVATE_LABEL_PURCHASE"
            const val TAP_VIEW_PRIVATE_LABEL = "TAP_VIEW_PRIVATE_LABEL"

            const val TAP_ACTIVE_ENQUIRIES_TAB = "tap_active_enquiries_tab"
            const val TAP_GARAGE_BID_HISTORY_TAB = "tap_garage_bid_history_tab"
            const val SELECT_FUEL = "select_fuel"
            const val TAP_BID_ENQUIRY = "tap_bid_enquiry"
            const val TAP_BID_ACCEPTED = "tap_bid_accepted"
            const val TAP_BID_HISTORY = "tap_bid_history"
            const val TAP_BID_SEND_TO_GARAGE = "tap_bid_send_to_garage"
            const val TAP_ORDERS_TO_PICKUP = "orders_to_pickup_walkin"
            const val SELECT_TIME_BUCKET = "select_time_bucket"
            const val CHANGE_TIME_SLOT = "change_time_slot"
            const val CHOOSE_DRIVER = "driver_choose"
            const val VIEW_CART = "view_cart"
            const val ADD_TO_CART = "add_to_cart"
            const val TAP_VOICE_SEARCH = "tap_voice_search"
            const val VOICE_SEARCH = "voice_search"
            const val VOICE_SEARCH_TERM = "voice_search_term"
            const val REMOVE_CART = "remove_cart"
            const val OPEN_ORDER = "open_order"
            const val SEND_OTP = "send_otp"
            const val OTP_FILLED = "otp_filled"
            const val UPDATE = "update"
            const val UPDATE_NOTIFY = "update_notify"
            const val ADD_PARTS_SERVICE_PACKAGE = "add_parts_service_package"
            const val SEARCH_PART = "search_part"
            const val SEARCH_CLICK = "search_click"
            const val PART_SERVICE_ADDED = "part_service_added"
            const val SEND_TO_CR = "send_to_cr"
            const val PAYMENT_SAVE = "payment_save"
            const val MARK_DELIVERED = "mark_delivered"
            const val SEND_ENQUIRY = "send_enquiry"
            const val SEARCH_EVENT = "search_event"
            const val TAP_CALL_TO_GARAGE = "tap_call_to_garage"
            const val TAP_LOCATION_TO_GARAGE = "tap_location_to_garage"
            const val TAP_NEW_BID = "tap_new_bid"
            const val TAP_BID_CREATE_ORDER = "tap_create_order"
            const val TAP_BID_PLACE_ORDER = "confirm_order"
            const val TAP_ACCEPT_REJECT = "tap_accept_reject"
            const val TAP_ACCEPT_ALL = "tap_accept_all"
            const val TAP_REJECT_ALL = "tap_reject_all"
            const val TAP_PRICE_PER_ITEM = "tap_price_per_item"
            const val TAP_ADD_ITEM = "tap_add_item"
            const val EDIT_QUANTITY = "edit_quantity"
            const val TAP_EDIT_QUANTITY_KEYBOARD = "tap_key_edit_quantity"
            const val TAP_EDIT_QUANTITY = "tap_edit_quantity"
            const val PRICE_PER_ITEM = "price_per_item"
            const val ADD_PRICE_PER_ITEM = "add_price_per_item"
            const val TAP_ACCEPTED_BID = "tap_accepted_bid"
            const val TAP_SELECT_DATE = "tap_select_date"
            const val TAP_SELECT_TIME = "tap_select_time"
            const val TAP_GOLD_BANNER = "tap_gold_banner"
            const val SELECT_NEW_VARIANT = "select_new_variant"
            const val SELECT_ABS = "select_abs"
            const val SELECT_TRANSMISSION = "select_transmission"
            const val SELECT_TAX_BRACKET = "select_tax_bracket"
            const val VIEW_PL_ITEM = "view_pl_item"
            const val ORDER_STATUS = "order_status"
            const val TAP_BRAND = "select_car_brand"
            const val TAP_MODEL = "select_car_model"
            const val TAP_YEAR = "select_car_year"
            const val TAP_FUEL = "select_engine_type"
            const val TAP_CATEGORY = "tap_category"
            const val TAP_TRACK_ORDER = "tap_track_order"
            const val TAP_TRACK_ORDER_DETAIL = "tap_track_order_detail"
            const val TAP_TRACK_ORDER_DOCKET = "tap_order_docket_search"

            const val TAP_SEARCH_BRAND = "tap_search_brand"
            const val TAP_SEARCH_MODEL = "tap_search_model"
            const val TAP_SEARCH_YEAR = "tap_search_year"
            const val TAP_SEARCH_FUEL = "tap_search_fuel"

            const val TAP_PRIVATE_LABEL_TRACKING_BANNER = "TAP_PRIVATE_LABEL_TRACKING_BANNER"

            const val TAP_DOWNLOAD_PL_INVOICE = "tap_download_pl_invoice"
            const val TAP_DIRECT_AND_MISSED_BUCKET = "tap_direct_missed_bucket"
            const val CLICK_LOGIN_WHATSSAPP = "click_login_whatsapp"
            const val GST_DOWNLOAD_DOC = "gst_download_doc"
            const val UPLOAD_PANCARD = "upload_pancard"
            const val UPLOADED_PANCARD = "pancard_uploaded"
            const val UPLOAD_GST_CERTIFICATE = "upload_gst_certificate"
            const val TAP_GST_DOWNLOAD_DOC = "tap_gst_download_doc"
            const val TAP_UPLOAD_PANCARD = "tap_upload_pancard"
            const val GST_SCREEN = "gst_screen"
            const val UPLOAD_BULK_INVENTORY = "upload_bulk_inv"
            const val CHANGE_WX_RET = "change_wx_ret"
            const val SELECT_DISTRIBUTOR = "select_distributor"
            const val EVENT_VIEW_ITEMS="gar_view_cart"
            const val EVENT_CONTINUE_CART="gar_continue_cart"

            const val TAP_BLOG = "tap_blog"
            const val TAP_VIDEO = "tap_video"
            const val GA_ADD_ITEM = "gar_add_item"
            const val VIEW_CUSTOMER_REVIEW = "view_customer_review"
            const val DA_LEAD_ACTION = "da_lead_action"
            const val CALL_THREE_LINE_BUTTON = "call_three_line_menu"
            const val CALL_THREE_DOT_BUTTON = "call_three_dot_menu"
        }
    }

    abstract class FirebaseParamsENUM private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val VALUE = "value"
            const val METHOD = "method"
            const val ROLE = "role"
            const val FIRE_SCREEN = "fire_screen"
            const val SEARCH_TERM = "search_term"
            const val SETTLE_TABS = "settle_tabs"
            const val BILL_TYPE = "bill_type"
            const val OTP = "otp"
            const val DASHBOARD = "dashboard"
            const val DASHBOARD_RETAILER = "dashboard_retailer"
            const val DASHBOARD_DISTRIBUTOR = "dashboard_distributor"
            const val SUPPORT = "support"
            const val TRAINING = "training"
            const val INCENTIVES = "incentives"
            const val LATEST_APK_LINK = "latest_apk_link"
            const val ALL_ORDERS = "all_orders"
            const val SPARES_ANALYSIS = "spares_analysis"
            const val SPARES_ANALYSIS_DETAILS = "spares_analysis_details"
            const val EXPENSE_TRACKER = "expense_tracker"
            const val EXPENSE_TRACKER_DETAILS = "expense_tracker_details"
            const val EXPENSE_TRACKER_ADD_CLICKED = "expense_tracker_add_clicked"
            const val SPARES_ANALYSIS_YEAR_CLICKED = "spares_analysis_year_clicked"
            const val SEARCH_ORDER = "search_orders"
            const val MOBIL = "mobil"
            const val SPARE_TRACK = "spare_track"
            const val SPARE_TRACK_DETAILS = "spare_track_details"
            const val EMPLOYEE_PROFILE = "employee_profile"
            const val EMPLOYEE_PROFILE_DISTRIBUTOR = "employee_profile"
            const val ORDER_SPARES = "order_spares"
            const val WALLET_FRAGMENT = "wallet_fragment"
            const val WALLET_FRAGMENT_RETAILER = "wallet_fragment_retailer"
            const val DOWNLOAD_ORDER_HISTORY = "download_order_history"
            const val DOWNLOAD_WALLET_HISTORY = "download_wallet_history"
            const val ORDER_DETAILS = "order_details"
            const val DRIVER_ORDER_DETAILS = "driver_order_details"
            const val INSURANCE_ORDER_DETAILS = "insurance_order_details"
            const val FILES = "files"
            const val JOB_CARD = "job_card"
            const val INVENTORY = "inventory"
            const val ESTIMATE = "estimate"
            const val ESTIMATE_DETAILS_ITEM = "estimate_item"
            const val SPARES = "spares"
            const val BULK_SPARES = "bulk_spares"
            const val HEALTH_CARD = "health_card"
            const val PAYMENT = "payment"
            const val SEARCH_PART_SERVICES = "search_parts"
            const val SEARCH_SPARES = "search_spares"
            const val SEARCH_PART_SERVICES_WORK_TYPE = "search_parts_work_type"
            const val SEARCH_CATEGORIES = "search_categories"
            const val SPARE_ORDER_LIST = "spare_orders_list"
            const val SPARE_ORDER_DETAILS = "spare_order_details"
            const val ADD_PAYMENT = "add_payment"
            const val SIGNATURE = "signature"
            const val PAYMENT_ADD_REMARK = "payment_add_remark"
            const val PAYMENT_MARK_COMPLETE = "payment_mark_complete"
            const val UPDATE_PAGE = "update_page"
            const val VERSION_NAME = "version_name"
            const val MASK_VERIFICATION = "mask_verification"
            const val TRANSACTION_HISTORY = "transaction_history"
            const val TRANSACTION_SUMMARY = "transaction_summary"
            const val TRANSACTION_SUMMARY_RETAILER = "transaction_summary_retailer"
            const val DRIVER_ORDER_LIST = "order_list"
            const val ORDER_LIST = "order_list"
            const val DRIVER_ORDER_LIST_TYPE = "order_list_type"
            const val VALET_SCREEN = "valet_card"
            const val ESCALATION_HISTORY = "escalation_history"
            const val NPS_DETAILS = "nps_details"
            const val ORDER_BUCKETS = "order_bucket_page"
            const val ALL = "all"
            const val SETTLEMENTS_FRAGMENT = "settlements"
            const val SETTLEMENTS_FRAGMENT_RETAILER = "settlements_retailer"
            const val SETTLEMENTS_FRAGMENT_DISTRIBUTOR = "settlements_distributor"
            const val SETTLEMENTS_REMARKS_FRAGMENT = "settlement_remarks"
            const val ASSIGN_DRIVER = "assign_drivers"
            const val FEEDBACK_DETAILS = "feedback_details"
            const val FEEDBACK_QUESTIONS = "feedback_questions"
            const val GST_IGST_PAGE = "delivery_time_page"
            const val ORDER_HISTORY = "order_history"
            const val ORDER_LIST_FILTER = "order_list_filter"
            const val ORDER_BOTTOM_MENU = "order_bottom_menu"
            const val ORDER_SEARCH_BOTTOM = "order_search_debug"
            const val SURVEYOR_BILL = "surveyor_bill"
            const val CUSTOMER_BILL = "customer_bill"
            const val UPGRADE_LIST = "upgrade_list"
            const val COMPATIBILITY_LIST = "compatibility_list"
            const val BRAND_LIST = "brand_list"
            const val SPARE_TRANSACTION_HISTORY = "spare_transaction_history"
            const val SPARE_FILTER = "spare_filter"
            const val SPARES_PAYMENT = "spare_payment"
            const val SPARES_PAYMENT_SUCCESS = "spare_payment_success"
            const val GARAGE_ORDERS = "garage_orders"
            const val GARAGE_INVENTORY = "garage_inventory"
            const val CREATE_ENQUIRY = "create_enquiry"
            const val NOTIFICATIONS = "notifications"
            const val NOTIFICATION_TYPE = "notification_type"
            const val EMPLOYEE_PROFILE_ADD_EDIT = "employee_profile_details"
            const val EMPLOYEE_PROFILE_ADD_EDIT_DISTRIBUTOR = "employee_profile_details_distributor"
            const val PROFILE_IMAGE_UPLOADED = "profile_image_uploaded"
            const val COUNTRY_CODE = "country_code"
            const val PLACE_TOP_SPARE_ORDER = "place_top_spare_order"
            const val PENALTY_REWARDS_ANALYTICS = "penalty_rewards_analytics"
            const val MAKE_RECON_REQUEST_FRAGMENT = "make_recon_request"
            const val MAKE_RECON_REQUEST_FRAGMENT_RETAILER = "make_recon_request_retailer"
            const val RECON_REQUEST_SUCCESS = "recon_request_success"
            const val RETAILER_RECON_REQUEST_SUCCESS = "recon_request_success_retailer"
            const val PAYMENT_DETAILS = "payment_details"
            const val PAYMENT_DETAILS_RETAILER = "payment_details_retailer"
            const val PAYMENT_DETAILS_DISTRIBUTOR = "payment_details_distributor"
            const val RETAILER_LIST = "retailer_list"
            const val WORKSHOPS_LIST = "workshops_list"
            const val DISTRIBUTOR_LIST = "distributor_list"
            const val TAB_TYPE = "tab_type"
            const val LEADERBOARD = "leaderboard"
            const val WORKSHOP_CALENDAR = "workshop_calendar"
            const val ARRIVAL_MODE = "arrival_mode"
            const val CAR_INFO_DETAILS = "car_details_info"
            const val GEO_FENCE_EVENT_FAILURE_REASON = "failure_reason"
            const val GEO_TRANSITION_TYPE = "geofence_transition_type"
            const val GEO_FENCE_EVENT_ENTRY = "geofence_event_entry"
            const val GEO_FENCE_EVENT_EXIT = "geofence_event_exit"
            const val RAISE_INVENTORY_REQUEST = "raise_inventory_request"
            const val INVENTORY_REQUEST_RAISED = "inventory_request_raised"

            const val MOENGAGE_NPS_VALUE = "moengage_nps_value"
            const val MOENGAGE_LEADERBOARD_SCORE = "moengage_leaderboard_score"
            const val MOENGAGE_LEADERBOARD_POSITION = "moengage_leaderboard_position"
            const val MOENGAGE_SETTLEMENTS_PENDING_COUNT = "moengage_settlements_pending_count"
            const val MOENGAGE_ESCALATIONS_PENDING_COUNT = "moengage_escalations_pending_count"

            const val CREATE_ORDER_SCREEN = "create_order_screen"
            const val CREATE_ORDER_SCREEN_DISTRIBUTOR = "create_order_screen_distributor"
            const val ORDER_HISTORY_SCREEN = "order_history_screen"
            const val ADD_PART_SCREEN = "add_part_screen"
            const val ADD_PART_SCREEN_DISTRIBUTOR = "add_part_screen_distributor"
            const val ADD_INVENTORY_SCREEN = "add_inventory_screen"
            const val ADD_INVENTORY_SCREEN_DISTRIBUTOR = "add_inventory_screen_distributor"
            const val ADD_RACK_ITEM_SCREEN = "add_rack_item_screen"
            const val ADD_RACK_ITEM_SCREEN_DISTRIBUTOR = "add_rack_item_screen_distributor"
            const val CART_SUCCESS_FULLY_ADDED = "cart_successfully_added"
            const val PART_SUCCESS_FULLY_ADDED_FROM_WAREHOUSE =
                "part_successfully_added_from_warehouse"
            const val RESEND_OTP = "resend_otp"
            const val TRAINING_VIDEOS_LIST = "training_videos_list"
            const val TRAINING_VIDEOS_VIEW = "training_videos_view"
            const val DASHBOARD_TRAINING = "training_dashboard"
            const val ORDER_DATE_FILTER = "order_date_filter"
            const val MCQ_TRAINING = "mcq_training"
            const val IS_MOBIL = "is_mobil"
            const val PRIVATE_LABEL_FRAGMENT = "PRIVATE_LABEL_FRAGMENT"
            const val PRIVATE_LABEL_ORDER_FRAGMENT = "PRIVATE_LABEL_ORDER_FRAGMENT"
            const val PRIVATE_LABEL_FRAGMENT_CART = "PRIVATE_LABEL_FRAGMENT_CART"
            const val PRIVATE_LABEL_ITEM_DETAIL_FRAGMENT =
                "PRIVATE_LABEL_FRAGMENT_ITEM_DETAIL_FRAGMENT"
            const val PDP_ITEM_DETAIL_FRAGMENT ="PDP_ITEM_DETAIL"
            const val PRIVATE_LABEL_CHECKOUT_FRAGMENT = "PRIVATE_LABEL_CHECKOUT_FRAGMENT"
            const val PRIVATE_LABEL_HISTORY_FRAGMENT = "PRIVATE_LABEL_HISTORY_FRAGMENT"
            const val PRIVATE_LABEL_SUMMARY_FRAGMENT = "PRIVATE_LABEL_SUMMARY_FRAGMENT"

            const val ACTIVE_HISTORY_ENQUIRIES_GARAGE = "active_history_garage_enquiries"
            const val CREATE_ENQUIRY_SCREEN = "create_enquiry_screen"
            const val CREATE_ENQUIRY_SCREEN_DISTRIBUTOR = "create_enquiry_screen_distributor"

            const val TAP_RETAILER_ENQUIRY = "tap_retailer_enquiry"
            const val RETAILER_ENQUIRY_FRAGMENT = "retailer_enquiry_fragment"
            const val RETAILER_BID_ACCEPTED_SCREEN = "retailer_bid_accepted_screen"
            const val RETAILER_BID_HISTORY_SCREEN = "retailer_bid_history_screen"
            const val RETAILER_BID_HISTORY_SUMMARY_SCREEN = "retailer_bid_history_screen"
            const val RETAILER_BID_SCREEN = "retailer_enquiry_screen"
            const val RETAILER_ACCEPTED_SCREEN = "retailer_accepted_screen"
            const val RETAILER_BID_CREATE_ORDER_SCREEN = "create_order"

            const val WARRANTY_VIDEOS_VIEW = "warranty_videos_view"

            const val LEAD_LIST_V2 = "lead_list_new"
            const val GOLD_PROGRAM = "gold_program"
            const val ORDER_SUMMARY = "order_summary"
            const val PRODUCT_NAME = "product_name"
            const val BANK_LIST_SCREEN = "bank_list_screen"
            const val GST_SUPPORT = "gst_support"
            const val LANGUAGE_SELECT = "langauge_select"
            const val BID_HISTORY = "bid_history"
            const val DIRECT_MISSED_LEADS = "direct_missed_leads"
            const val DIRECT_LEADS_BLOCKER = "direct_assigned_blocker"
            const val WHATSAPP = "whatsapp"
            const val UPLOAD_BULK_INVENTORY = "upload_bulk_inventory"
            const val DOWNLOAD_BULK_INVENTORY = "download_bulk_inventory"
            const val CONFIRM_BULK_INVENTORY = "upload_inv_confirm"
            const val BULK_UPLOAD_VIEW_INVENTORY = "bulk_upload_view_inv"
            const val UPLOAD_INV_FILE = "upload_inv_file"

            const val BLOGS_ARTICLES = "blog_articles"

        }
    }

    abstract class RefactoredStrings private constructor() {
        init {
            throw IllegalStateException("")
        }

        companion object {
            const val BASE_URL = "https://garage.gomechanic.app/"

            const val FROM_NOTIFICATION = "fromNotification"
            const val BILLS = "bills"
            const val HISTORY = "history"
            const val ESTIMATES = "estimates"
            const val JOBCARDS = "jobcards"
            const val BILL_PDF = "bill.pdf"
            const val TYPE = "type"
            const val ORDER_OF = "order_of"
            const val SELECTED_FILTER = "selected_filter"
            const val IS_INSURANCE = "is_insurance"
            const val OPEN_ESTIMATE = "open_estimate"
            const val OPEN_INVENTORY = "open_inventory"
            const val OPEN_PAYMENT = "open_payment"
            const val RETAIL = "retail"
            const val INCOMING = "incoming"
            const val OUTGOING = "outgoing"
            const val PICKUP_STRING = "pickup"
            const val RETAIL_CAPS = "RETAIL"
            const val DAYS = "days"
            const val MOBILE = "mobile"
            const val LEAD_MODEL = "leadModel"
            const val FLEET_ID = "fleet_id"
            const val LEAD_ID = "lead_id"
            const val CAR_TYPE_ID = "car_type_id"
            const val CAR_ID = "car_id"
            const val WALKIN_STRING = "walkin"
            const val ASSIGNED_STRING = "assigned"
            const val CAR_NAME = "car_name"
            const val ODOMETER_READING = "odometer_reading"
            const val REG_NUMBER = "reg_number"
            const val NAME = "name"
            const val PHONE = "phone"
            const val EMAIL = "email"
            const val USER_ID = "user_id"
            const val CAR_BRAND = "car_brand"
            const val CAR_MODELNAME = "car_modelname"
            const val SELECTED_CAR = "selected_car"
            const val ORDER_FROM = "order_from"
            const val SEND_TO_CR = "send_to_cr"
            const val INVOICE_NUMBER = "invoice_number"
            const val GARAGE_GSTIN = "garage_gstin"
            const val UPDATE_REQUEST_CODE = 999
            const val JOB_CARD_CREATED_FROM = "job_card_created_from"
            const val GARAGE_APP = "garage_app"
            const val PROVIDER = ".provider"
            const val APPLICATION_PDF = "application/pdf"
            const val APPLICATION_XLSX = "application/vnd.ms-excel"
            const val THIRTY = "30"
            const val NO = "no"
            const val ALL_ZEROES = "0000"
            const val GARAGE_APP_CAPS = "Garage App"
            const val GARAGE_APP_CAPS_NO_SPACES = "GarageApp"
            const val LAST_SLOT = "17:00"
            const val LAST_SLOT_WINDOW = "05 PM to 06 PM"
            const val PROD_TOPIC_PREFIX = "W_"
            const val DEV_TOPIC_PREFIX = "DEV_W_"
            const val STAGING_TOPIC_PREFIX = "STAGING_W_"

            const val DRIVER_PROD_TOPIC_PREFIX = "D_"
            const val DRIVER_DEV_TOPIC_PREFIX = "DEV_D_"
            const val DRIVER_STAGING_TOPIC_PREFIX = "STAGING_D_"

            const val R_PROD_TOPIC_PREFIX = "R_"
            const val R_DEV_TOPIC_PREFIX = "DEV_R_"
            const val R_STAGING_TOPIC_PREFIX = "STAGING_R_"


            const val R_PROD_TOPIC_PREFIX_DIS = "DIS_"
            const val R_DEV_TOPIC_PREFIX_DIS = "DEV_DIS_"
            const val R_STAGING_TOPIC_PREFIX_DIS = "STAGING_DIS_"

            const val PROD = "PROD"
            const val DEV = "DEV"
            const val IS_GARAGE_ORDER = "is_garage_order"
            const val IS_GARAGE_SELECTED = "is_garage_selected"
            const val CR_MOBILE = "cr_mobile"
            const val REG_NO = "reg_no"
            const val SELECT_YEAR = "Select year"
            const val YEAR_OF_MFG = "year_of_mfg"
            const val SKU_ORDER_ID = "sku_order_id"
            const val PRODUCT_NAME = "product_name"
            const val IS_BULK_ORDER = "is_bulk_order"
            const val BILL_PDF_TYPE = 1
            const val JOBCARD_PDF_TYPE = 2
            const val ESTIMATE_PDF_TYPE = 3
            const val SURVEYOR_ESTIMATE_PDF_TYPE = 5
            const val SURVEYOR_BILL_PDF_TYPE = 6
            const val SPARES_BILL_PDF_TYPE = 7
            const val SKU_ORDER_LIST = "sku_order_list"
            const val PLUS_CAPS_STRING_LEADING_SPACE = " PLUS"
            const val NULL_STRING = "null"
            const val BUG_FORM_URL = "https://forms.gle/JFsQEYJiXVpuabN2A"
            const val ORDER_TOTAL = "order_total"
            const val ENGINE_OIL = "Engine oil"
            const val FLAT_DISCOUNT = "flat_discount"
            const val TRACK = "track"
            const val ESTIMATED_DELIVERY = "estimated_delivery"
            const val REMARK = "remark"
            const val CAR_IMAGES_PATH_PREFIX = "cr_app_data/job_card_images/Fleet-"
            const val PAYMENT_IMAGES_PATH_PREFIX = "cr_app_data/payment_images/W-"
            const val PAYMENT_IMAGES_PATH_PREFIX_DISTRIBUTOR = "cr_app_data/payment_images/D-"
            const val DROP_IMAGES_PATH_PREFIX = "cr_app_data/drop_images/"
            const val GARAGE_CAR_WISE_IMAGES_PATH_PREFIX =
                "cr_app_data/garage_car_wise_part_images/W-"
            const val RETAILER_CAR_WISE_IMAGES_PATH_PREFIX =
                "cr_app_data/retailer_car_wise_part_images/R-"
            const val NEW_CAR_SNAPSHOTS = "NewCarSnapshots"
            const val CAR_SNAPSHOTS = "CarSnapshots"
            const val CLUSTER_SNAPSHOTS = "ClusterSnapshots"
            const val DELIVERY_SNAPSHOTS = "DeliverySnapshots/Order-"
            const val STICKER_SNAPSHOTS = "StickerSnapshots/Order-"
            const val PART_SNAPSHOTS = "PartSnapshots/Order-"
            const val SIGNATURE_JPG = "Signature.jpg"
            const val FLEET_PATH = "/Fleet-"
            const val CAR_PATH = "/Car-"
            const val ORDER_PATH = "/Order-"
            const val RECORDING = "/recording.3gp"
            const val CR_APP_DATA_PREFIX = "cr_app_data/"
            const val OLD = "old"
            const val NEW = "new"
            const val DELIVERY_V2 = "delivery"
            const val PART = "part"
            const val CLUSTER = "cluster"
            const val PAYMENT = "payment"
            const val FROM_GARAGE = "from_garage"
            const val SIGNATURE_IMAGE_URL = "signature_image_url"
            const val SIGNATURE_IMAGE_NAME = "signature_image_name"
            const val PRE_SERVICE_CAR_URLS = "pre_service_car_image_urls"
            const val PRE_SERVICE_CAR_NAMES = "pre_service_car_image_names"
            const val POST_SERVICE_CAR_URLS = "post_service_car_image_urls"
            const val POST_SERVICE_CAR_NAMES = "post_service_car_image_names"
            const val CLUSTER_CAR_URLS = "cluster_image_urls"
            const val CLUSTER_CAR_NAMES = "cluster_car_image_names"
            const val THREE_HUNDRED = "300"
            const val ORDER = "order"
            const val SELF = "self"
            const val DATE = "date"
            const val DAYS_PASSED = "days_passed"
            const val FILTER_ITEMS = " filter_items"
            const val WALKIN_FIRST_CAPS = "Walkin"
            const val PICKUP_FIRST_CAPS = "Pickup"
            const val GOMECHANIC_LEADS = "GoMechanic Leads"
            const val BUCKET = "bucket"
            const val ASSIGN = "assign"
            const val CONVERTED = "converted"
            const val PICKUP_COMPLETE_2 = "pickup complete"
            const val ORDERS_TO_FETCH = "orders_to_fetch"
            const val COMMISSION_BILL = "Commission Bill"
            const val GARAGE_BILL = "Garage Bill"
            const val CUSTOMER_BILL = "Customer Bill"
            const val BILL = "bill"
            const val OEM = "oem"
            const val OES = "oes"
            const val SPARES_IMAGE_PREFIX_PATH = "cr_app_data/spares_images/order-"
            const val SKUID_PATH = "sku-id"
            const val PENDING = "pending"
            const val PASSED_SEARCH_STRING = "passed_search"
            const val WORK_DONE = "work_done"
            const val ORDER_FROM_GOMECHANIC = "ORDER_FROM_GOMECHANIC"
            const val CAR_MAKE = "car_make"
            const val CAR_MODEL = "car_model"
            const val CAR_FUEL = "car_fuel"
            const val CAR_VARIANT = "car_variant"
            const val UPGRADE_LIST = "upgrade_list"
            const val COMPATIBILITY_LIST = "compatibility_list"
            const val IS_BILL = "is_bill"
            const val INSURANCE_COMPANY_ID = "insurance_company_id"
            const val BROKER_ID = "broker_id"
            const val SELLER_ADDRESS_ID = "seller_address_id"
            const val SURVEYOR_NAME = "surveyor_name"
            const val SURVEYOR_EMAIL = "surveyor_email"
            const val SURVEYOR_PHONE = "surveyor_phone"
            const val CLAIM_NO = "claim_no"
            const val POLICY_NO = "policy_no"
            const val POLICY_TYPE = "policy_type"
            const val DAMAGE_TYPE = "damage_type"
            const val PARTS_ORDERED_ETD = "parts_ordered_etd"
            const val INSURANCE_ORDER_OF = "insurance_order_of"
            const val NEW_CAR_TYPE_ID = "new_car_type_id"
            const val NO_ETD = "No ETD"
            const val APP_NAME = "ExpenseApp"
            const val DOWNLOAD_FILE = "download_file"
            const val PAYMENT_UPDATED = "Payment Updated"
            const val SELECT_BANK = "Select Bank"
            const val UPDATE_START_DATE = "Update start date"
            const val SIGNATURE_IMAGE = "Signature Image"
            const val DRIVER_RECORDING_LOCATION = "driver_recordings/"
            const val HEAD_REST = "Head Rest"
            const val FLOOR_MATS = "Floor Mats"
            const val WHEEL_CAPS = "Wheel Cap"
            const val MUD_FLAP = "Mud Flap"
            const val UPDATE_REMARK = "Update Remark"
            const val ORDER_ID_CAPS = "Order ID"
            const val GOMECHANIC_ORDERS = "GoMechanic Orders"
            const val GARAGE_ORDERS = "Garage Orders"
            const val JOB_CARD_SEARCH_PART = "Job Card search part"
            const val ESTIMATE_SEARCH_PART = "Order estimate search part"
            const val DELIVERY = "delivered"
            const val NO_DATA_FOUND = "No data found"
            const val IGST = "IGST"
            const val GST = "GST"
            const val SST = "SST"
            const val APPROVED_SMALL = "approved"
            const val PAID = "paid"
            const val ESCALATED_SMALL = "escalated"
            const val UPDATE_VIEW_IN_SETTLEMENT = "Update date view in settlement"
            const val ALL = "All"
            const val PENDING_FIRST_CAPS = "Pending"
            const val CLOSE_POPUP = "Close PopUp"
            const val YES = "Yes"
            const val NO_FIRST_CAPS = "No"
            const val INSURANCE_ADDRESS_ID = "insurance_address_id"
            const val SELECT_DAMAGE_TYPE = "Select Damage Type"
            const val BODY_SHOP = "BodyShop"
            const val TOTAL_LOSS = "Total Loss"
            const val SELECT_POLICY_TYPE = "Select Policy Type"
            const val COMPREHENSIVE = "Comprehensive"
            const val ZERO_DEBT = "0 Debt"
            const val LANGUAGE_CODE = "language_code"
            const val COUNTRY_CODE = "country_code"
            val languageNames = arrayOf("English", "हिंदी", "தமிழ்", "తెలుగు", "Bahasa Melayu")
            val languageCodes = arrayOf("en", "hi", "ta", "te", "ms")
            val countryIsoCodes = arrayOf("IN", "MYS")
            val countryDefaultLanguageCodes = arrayOf("en", "ms")
            val countryDialerCodes = arrayOf("+91", "+60")
            val countryNames = arrayOf("India", "Malaysia")
            const val IS_FROZEN = "is_frozen"
            const val REPLACED = "replaced"
            const val MRP = "mrp"
            const val IMAGE_PICKER_REQUEST_CODE_INVENTORY = 123
            const val IMAGE_PICKER_REQUEST_CODE_FILE = 124
            const val IMAGE_PICKER_REQUEST_CODE_SPARES = 125
            const val IMAGE_PICKER_REQUEST_CODE_MASK = 126
            const val IMAGE_PICKER_REQUEST_CODE_MASK_2 = 226
            const val IMAGE_PICKER_REQUEST_CODE_EMPLOYEE = 227
            const val IMAGE_PICKER_REQUEST_CODE_PAYMENT = 228
            const val IMAGE_PICKER_REQUEST_CODE_DROP = 229
            const val IMAGE_PICKER_REQUEST_CODE_ESTIMATE_SCANNER = 230
            const val IMAGE_PICKER_REQUEST_CODE_ENQUIRY_CART = 231
            const val IMAGE_PICKER_REQUEST_CODE_RETAILER_BID = 232
            const val IMAGE_PICKER_REQUEST_CODE_MASK_DASHBOARD = 233
            const val IMAGE_PICKER_REQUEST_CODE_ESTIMATE = 234
            const val APP_VERSION = "app_version"
            const val IS_DEBUG = "is_debug"

            const val SPARE_ORDER_ID = "spare_order_id"
            const val DUE_AMOUNT = "due_amount"
            const val PLUTUS_TRANSACTION_ID = "plutus_transaction_id"
            const val SHOW_POS_PAYMENT = "show_pos_payment"
            const val RETAILER_REWARD = "retailer_reward"
            const val DISTRIBUTOR_REWARD ="distributor_reward"
            const val RETAILER_ENQUIRY = "retailer_enquiry"
            const val SELECTED_EVENT = "rack_event"
            const val RACK_NAME = "rack_name"
            const val RACK_ID = "rack_id"
            const val SELECTED_WORKSHOP_NAME = "selected_workshop_name"
            const val APP_BUILD = "app_build"

            const val DEV_BUILD_TYPE = "Dev"
            const val PROD_BUILD_TYPE = "Prod"
            const val STAGING_BUILD_TYPE = "Staging"

            val PART_SEARCH_URLS = HashMap<String, String>().apply {
                put("DEV", "https://api.parts-rocket.com/")
                put("PROD", "https://api.parts-rocket.com/")
                put("STAGING", "https://api.parts-rocket.com/")
            }

            val MALAYSIA_URLS = HashMap<String, String>().apply {
                put("DEV", "https://mly.dev.garage.gomechanic.app/")
                put("PROD", "https://mly.garage.gomechanic.app/")
                put("STAGING", "https://mly.dev.garage.gomechanic.app/")
            }

            val COUNTRY_BASE_URLS = HashMap<String, HashMap<String, String>>().apply {
                put(INDIA_COUNTRY_CODE, HashMap<String, String>().apply {
                    put("DEV", "https://dev.garage.gomechanic.app/")
                    put("PROD", "https://garage.gomechanic.app/")
//                    put("PROD", "http://98.70.13.23:8080/")
                    put("STAGING", "https://staging.garage.gomechanic.app/")
                })
                put(MALAYSIA_COUNTRY_CODE, HashMap<String, String>().apply {
                    put("DEV", "https://mly.dev.garage.gomechanic.app/")
                    put("PROD", "https://mly.garage.gomechanic.app/")
                    put("STAGING", "https://mly.dev.garage.gomechanic.app/")
                })
            }

            val APP_BUILDS = arrayOf("Dev", "Prod", "Staging")

            const val WORKSHOP_ID_HEADER = "X-Workshop-Id"

            const val ASSIGNED_ORDERS = "ASSIGNED PICKUPS"
            const val COMPLETED_PICKUPS = "COMPLETED PICKUPS"

            const val IS_DRIVER = "is_driver"
            const val IS_RETAILER = "is_retailer"
            const val IS_INTRO_SHOWN = "IS_INTRO_SHOWN"
            const val IS_DISTRIBUTOR = "is_distributor"
            const val IS_DISTRIBUTOR_ACC = "is_distributor_acc"
            const val IS_ACC_DISTRIBUTOR = "is_distributor_acc"
            const val IS_DISTRIBUTOR_SELECTED = "is_distributor_selected"
            const val IS_DISTRIBUTOR_ADMIN = "is_distributor_admin"
            const val IS_SECONDARY_RETAILER = "is_secondary_retailer"

            const val DRIVER = "driver"
            const val SELF_HELP_REF = "self_help"
            const val SELF_HELP_VIDEOS_REF = "videos"
            const val SELF_HELP_DOCS_REF = "documents"

            const val ESCALATION_REMARKS = "escalation_remarks"
            const val ML_REMARKS = "ml_remarks"
            const val CUSTOMER_REMARKS = "customer_remarks"

            const val TITLE = "title"

            const val PASSIVE = "passive"
            const val PROMOTER = "promoter"
            const val DETRACTOR = "detractor"

            const val PROD_GARAGE_ADMIN = "PROD_GARAGE_ADMIN"
            const val STAGING_GARAGE_ADMIN = "STAGING_GARAGE_ADMIN"
            const val DEV_GARAGE_ADMIN = "DEV_GARAGE_ADMIN"

            const val PROD_PREFIX = "PROD_"
            const val STAGING_PREFIX = "STAGING_"
            const val DEV_PREFIX = "DEV_"

            const val OTP = "otp"
            const val IS_OTP_VERIFIED = "verified"

            const val ADVISOR = "advisor"
            const val IS_ADVISOR = "is_advisor"

            const val OTP_SENT = "otp_sent"
            const val OTP_VERIFIED = "otp_verified"

            const val IS_UI_BLOCKED = "is_ui_blocked"

            const val MIDNIGHT_ALARM_ID = 0

            const val ASSIGNED_DROPS = "ASSIGNED DROPS"

            const val CAN_CALL_MASKING_BE_BYPASSED = "can_call_masking_be_bypassed"

            const val LATEST_APK_LINK =
                "https://drive.google.com/file/d/1BaP3yh1topMb-uSN-9jsBYJjRe0Sx3G5/view?usp=sharing"

            const val WORKSHOP = "workshop"

            const val PICKUP_COMPLETE = "pickup_complete"

            const val VERSION_NAME = "version_name"

            const val UPDATES = "updates"

            const val GARAGE_APP_APK_PATH = "garage_app_latest_apk"

            const val BULK_SPARES = "BULK_SPARES"

            const val SHOW_VALET = "show_valet"

            const val REQUESTED_GOM_SMALL = "requested"

            const val SETTLEMENTS_AND_PAYMENT_SUMMARY_SMALL = "settlements & payment summary"

            const val SETTLED_BILLS_SMALL = "approved_by_finance"

            const val SETTLEMENT_SUMMARY = "Settlement Summary"

            const val PAYMENT_SUMMARY = "Payment Summary"

            const val MONTH = "month"

            const val YEAR = "year"

            const val PAGE_LIMIT = "page_limit"

            const val BARREL = "barrel"

            const val CAN = "can"

            const val BARREL_SIZE = "208 L"

            const val CAN_SIZE = "5 L"

            const val MOBIL_BILLS = "mobil_bills"

            const val SERVICES = "services"

            const val MTD = "mtd"

            const val MASK_VERIFIED = "mask_verified"

            const val LOGO_VERIFIED = "logo_verified"

            const val FEEDBACK_SIGNATURE_IMAGES_PATH_PREFIX = "cr_app_data/feedback_signatures"

            const val DEV_URL_HOST = "dev.garage.gomechanic.app"
            const val STAGING_URL_HOST = "staging.garage.gomechanic.app"
            const val PROD_URL_HOST = "garage.gomechanic.app"

            const val SHOW_INCENTIVE_SCREEN = "show_incentive_screen"
            const val REPLACE = "replace"
            const val MOBIL = "Mobil"
            const val USE_HALF_SLOTS = "use_half_slots"
            const val WORKSHOP_LATITUDE = "workshop_latitude"
            const val WORKSHOP_LONGITUDE = "workshop_longitude"
            const val SHOW_TIMERS = "show_timers"
            const val HIDE_SPARES = "hide_spares"
            const val PICKUP_TIMER = "Pickup Timer: "
            const val PICKUP_DELAY = "Pickup Delay: "
            const val DROP_TIMER = "Drop Timer: "
            const val DROP_DELAY = "Drop Delay: "

            const val IS_DROP = "is_drop"

            const val PICKUP_THRESHOLD = "pickup_threshold"
            const val WALKIN_THRESHOLD = "walkin_threshold"
            const val LOCATION_ACCURACY = "location_accuracy"
            const val PICKUP_CHECK_ENABLED = "pickup_check_enabled"
            const val WALKIN_CHECK_ENABLED = "walkin_check_enabled"

            const val SHOWN_FORCEFULLY = "shown_forcefully"
            const val SHOWN_FORCEFULLY_INSURANCE = "shown_forcefully_insurance"
            const val IS_OWNER = "is_owner"

            const val ESCALATION_HISTORY = "escalation_history"
            const val RETAILER_MODEL = "retailer_model"

            const val IS_PATH = "is_path"
            const val AAROGYA_SETU_PACKAGE = "nic.goi.aarogyasetu"

            const val SPECIALIZATION_LIST = "specialization_list"
            const val INFO = "info"

            const val SPARES_PURCHASED = "SPARES_PURCHASED"

            const val EASTER_EGGS_ON = "EASTER_EGGS_ON"

            const val SELECTED_WORKSHOP_ID = "SELECTED_WORKSHOP_ID"
            const val IS_PREVIOUS_WORKSHOP_SELECTED_BY_DISTRIBUTOR = "IS_PREVIOUS_SELECTED_BY_DISTRIBUTOR"
            const val IS_PREVIOUS_RETAILER_SELECTED_BY_DISTRIBUTOR = "IS_PREVIOUS_RETAILER_SELECTED_BY_DISTRIBUTOR"

            const val WALLET_BALANCE_CHANGED = "WALLET_BALANCE_CHANGED"

            const val SHOW_BOTTOM_CREATE_ENQUIRY = "SHOW_BOTTOM_CREATE_ENQUIRY"
            const val SHOW_BOTTOM_SETTLEMENTS = "SHOW_BOTTOM_SETTLEMENTS"
            const val SHOW_BOTTOM_WALLET = "SHOW_BOTTOM_WALLET"
            const val SHOW_BOTTOM_PAYMENTS = "SHOW_BOTTOM_PAYMENTS"

            const val BOTTOM_TAB1_LABEL = "BOTTOM_TAB1_LABEL"
            const val BOTTOM_TAB2_LABEL = "BOTTOM_TAB2_LABEL"
            const val BOTTOM_TAB3_LABEL = "BOTTOM_TAB3_LABEL"
            const val BOTTOM_TAB4_LABEL = "BOTTOM_TAB4_LABEL"
            const val BOTTOM_TAB5_LABEL = "BOTTOM_TAB5_LABEL"

            const val TYPE_ALL_ORDERS = "TYPE_ALL_ORDERS"
            const val INVOICES = "invoices"

            const val HIGHLIGHT_TEXT_DEFAULT = "GoMechanic Exclusive"
            const val HIDE_PHONE = "hide_phone"
            const val DATA = "data"
            const val ORDER_FILTER = "order_filter"
            const val IS_DOWNLOAD = "is_download"
            const val FROM_GARAGE_APP = "from_garage_app"

            const val MY_DROPS = "My Drops"
            const val ALL_DROPS = "All Drops"
            const val COMPLETED_DROPS = "Complete Drops"
            const val ASSIGNED_TO = "assigned_to"
            const val ME = "me"
            const val ALL_SMALL = "all"
            const val COMPLETED_SMALL = "completed"

            const val CATEGORY_ID = "category_id"
            const val CATEGORY_NAME = "category_name"

            const val SHOW_VERIFY = "show_verify"
            const val EDIT_MODE = "edit_mode"
            const val EMP_MODEL = "emp_model"

            const val EMP_PROFILE_IMAGES_PATH_PREFIX = "garage_app_data/profile_images"
            const val SEND_ENQUIRY_PATH = "garage_app_data/send_enquiry"
            const val EMP_PROFILE_IMAGE_NAME = "profile_image.jpg"

            const val CALL_MASKING = "call_masking"
            const val CALL_MASKING_INSURANCE = "call_masking_insurance"
            const val ALLOW_CALL = "allow_call"

            const val TAB_VIDEOS = "videos"
            const val TAB_DOCUMENTS = "documents"
            const val CAR_VARIANT_ID = "car_variant_id"
            const val CAR_VARIANT_NAME = "car_variant_name"
            const val MECHANIC_ID = "mechanic_id"
            const val MECHANIC_NAME = "mechanic_name"
            const val IS_CREDIT = "is_credit"
            const val INDIA_COUNTRY_CODE = "IN"
            const val MALAYSIA_COUNTRY_CODE = "MYS"
            const val IMAGE_URL = "image_url"
            const val SHOW_OWN_PROFILE = "shown_own_profile"

            const val TOP_SPARE_MODEL = "top_spare_model"
            const val RECON_REQUEST = "recon_request"
            const val MAKE_PAYMENT = "make_payment"
            const val IS_BIFURCATION = "is_bifurcation"
            const val IS_GARAGE_PAYMENTS_PAGE = "is_garage_payments_page"

            const val CMS_WARNING_MESSAGE = "cms_warning_message"
            const val CMS_WARNING_DESCRIPTION = "cms_warning_description"

            const val RECON_DETAILS = "Recon Details"
            const val PAYMENT_HISTORY = "Payment History"
            const val PART_NAME = "part_name"
            const val QUANTITY = "quantity"

            const val SPARES_OTP_REF = "spares_purchase_otp"
            const val TSHIRT_PENALTY_AMOUNT = "tshirt_penalty_amount"
            const val TSHIRT_BLOCK = "tshirt_block"
            const val DROP_OTP_MANDATORY = "drop_otp_mandatory"

            const val CONSOLIDATED_INVOICES_API = "workshop/invoices/consolidate-invoice/download/"
            const val GET_BILL_API = "workshop/order/get_bill/"
            const val ANNOUNCEMENTS_TAB = "announcement"
            const val ALERTS_TAB = "alert"
            const val PROFILE_TAB = "profile"
            const val RATINGS_TAB = "ratings"

            const val CHAT_REMARK = "chat_remarks"
            const val PREVIOUS_ORDER_REMARK = "previous_order_remarks"
            const val DROP_DELIVERY_IMAGES = "drop_delivery_images"
            const val PART_USED_IMAGES = "part_used_images"

            const val DROP_DELIVERY_IMAGES_PATH_PREFIX = "cr_app_data/drop_delivery_images/Order-"
            const val PART_USED_IMAGES_PATH_PREFIX = "cr_app_data/part_used_images/Order-"

            const val IS_AMC = "is_amc"
            const val START_MONTH = "start_month"
            const val END_MONTH = "end_month"
            const val MECHANIC_CHECK_ENABLED = "mechanic_check_enabled"
            const val RECON_REQUEST_PERCENTAGE = "recon_request_percentage"
            const val SHOW_SPARE_CREDIT_DATA = "show_spare_credit_data"
            const val SPARE_CREDIT_DATA = "spare_credit_data"

            const val HAS_RETAIL = "has_retail"
            const val HAS_INSURANCE = "has_insurance"

            const val FROM_NOTIFICATION_PANEL = "fromNotificationPanel"
            const val JOB_CARD_LOCATION_CHECK_ENABLED = "job_card_location_check_enabled"
            const val GEOFENCING_ENABLED = "geofencing_enabled"

            const val GEO_FENCE_REQUEST_ID = 919
            const val GEO_FENCE_PENDING_REQUEST = 920

            const val GEO_FENCE_CREATED = "geofence_created"
            const val PIE_CHART_CREDIT = "CREDIT"
            const val PIE_CHART_DEBIT = "DEBIT"

            const val CAR_PARKING_INFO = "car_parking_info"
            const val LEADERBOARD = "leaderboard"
            const val SPARE_ANALYSIS = "spare_analysis"
            const val OPEN_PAYMENT_SUMMARY = "open_payment_summary"
            const val OPEN_SETTLEMENTS_APPROVAL = "open_settlements_approval"
            const val OPEN_SETTLEMENTS_ESCALATIONS = "open_settlements_escalations"
            const val OPEN_GOLD_PARTNER_PROGRAM = "open_gold_partner_program"
            const val OPEN_SETTLEMENTS = "open_settlements"
            const val OPEN_GOLD_PARTNER = "open_gold_partner"
            const val OPEN_CREATE_BID = "open_create_bidr"
            const val OPEN_CHECKOUT_RETAILER = "open_checkout_retailer"
            const val OPEN_CHECKOUT = "open_checkout"
            const val OPEN_VIEW_CART_RETAILER_RETAILER = "open_view_cart_retailer"
            const val OPEN_VIEW_CART = "open_view_cart"
            const val OPEN_PRIVATE_LABEL_DETAIL_RETAILER = "open_private_label_detail_retailer"
            const val OPEN_PRIVATE_LABEL_RETAILER = "open_private_label_retailer"
            const val OPEN_PRIVATE_LABEL = "open_private_label"
            const val GOLD_PARTNER_URL = "gold_partner_program_url"
            const val OPEN_TYPE = "open_type"
            const val WHATSAPP_IMAGES = "whatsapp_images"
            const val INTERIOR_EXTERIOR = "interior_exterior"
            const val IMAGE_TYPE = "image_type"

            const val WHATSAPP_PACKAGE = "com.whatsapp"
            const val MOENGAGE_SIGNED_IN = "moengage_signed_in"

            const val CITY_ID = "city_id"
            const val RETAILER_ID = "retailer_id"
            const val DISTRIBUTOR_ID = "distributor_id"
            const val DISTRIBUTOR_NAME = "distributor_name"
            const val RETAILER_USER_ID = "retailer_user_id"
            const val RETAILER = "retailer"
            const val DISTRIBUTOR = "distributor"
            const val CENTRAL_DISTRIBUTOR = "distributor_central"
            const val SECONDARY_DISTRIBUTOR = "distributor_manager"
            const val DISTRIBUTOR_ADMIN = "distributor_admin"
            const val RETAILER_NAME = "retailer_name"
            const val SETTLED = "settled"
            const val RECON_TYPE = "recon_type"
            const val RECON_AMOUNT= "recon_amount"
            const val SPARES_RETAILER = "spares_retailer"
            const val SECONDARY_SPARES_RETAILER = "secondary_spares_retailer"
            const val LOGIN_TYPE = "login_type"

            const val COMPANY_NAME = "comapny_name"
            const val BANK_NAME = "bank_name"
            const val BANK_BRANCH_ADDRESS = "bank_branch_address"
            const val IFSC_CODE = "ifsc_code"
            const val BANK_ACCOUNT = "bank_account_no"

            const val COMPANY_NAME_VAL = "Targetone Innovations Private Limited"
            const val BANK_NAME_VAL = "ICICI BANK LTD."
            const val BANK_BRANCH_ADDRESS_VAL = "SHOP NO 108 TO 111 PARAS TRINITY SECTOR 63 GURGAON"
            const val IFSC_CODE_VAL = "ICIC0007241"
            const val BANK_ACCOUNT_VAL = "724105000088"

            const val DELIVERY_IMAGE = "delivery_image"
            const val MECHANIC = "mechanic"
            const val SUPERVISOR = "supervisor"
            const val IS_MECHANIC = "is_mechanic"
            const val FROM_MOENGAGE = "from_moengage"
            const val IS_EVENT_TRACKED = "is_event_tracked"

            const val SALES_INVOICE = "Sales Invoice"
            const val PURCHASE_INVOICE = "Purchase Invoice"

            const val CURRENT_MONTH = "current_month"
            const val CURRENT_YEAR = "current_year"
            const val WALKIN_SELFIE_CHECK = "walkin_selfie_check"

            const val DELIVERY_CHECKS = "delivery_checks"
            const val DELIVERY_CHECKS_SELECTED_IDS = "delivery_checks_selected_ids"

            const val TSHIRT_PENALTY_AMOUNT_WALKIN = "tshirt_penalty_amount_walkin"
            const val CAR_FILE_IMAGES = "delivery_image"
            const val CAR_USED_FILE_IMAGES = "used_parts_image"

            const val TSHIRT_BLOCK_WALKIN = "tshirt_block_walkin"

            const val OPEN_JOB_CARD = "open_job_card"
            const val ESTIMATE_DISCLAIMER = "estimate_disclaimer"
            const val EDIT_ITEM = "edit_item"
            const val ADD_PHOTO = "add_photo"
            const val EDIT_ITEM_API = "edit_item_api"
            const val DELETE_ITEM = "delete_item"
            const val SAVE_ITEM = "save_item"
            const val ON_RACK_CLICKED = "on_rack_clicked"
            const val ON_RACK_EDIT = "on_rack_edit"
            const val ON_RACK_ITEM_EDIT = "on_rack_item_edit"
            const val ON_RACK_ITEM_DELETE = "on_rack_item_delete"
            const val ON_RACK_ITEM_ADD = "on_rack_item_add"
            const val ON_RACK_ITEM_MINUS = "on_rack_item_minus"
            const val SEARCH_PART_MODEL = "search_part_model"
            const val ADD_INVENTORY_CART = "add_inventory_cart"
            const val MINUS_INVENTORY_CART = "minus_inventory_cart"
            const val CART_LIST = "cart_list"

            const val BRAND = "brand"
            const val MODEL = "model"
            const val FUEL = "fuel"
            const val ITEMS_ADDED = "items_added"
            const val IS_INVENTORY_USED = "is_inventory_used"
            const val IS_RETAILER_LOG_IN = "is_retailer_login"
            const val ADVANCE_CREDIT = "advance_credit"
            const val SEARCH_QUERY = "query"
            const val INCOMPLETE = "Incomplete"
            const val INCOMPLETE_SMALL = "incomplete"

            const val LEVEL_ID = "level_id"
            const val TEST_ID = "test_id"
            const val VIDEO_URL = "video_url"
            const val VIDEO_ID = "video_id"
            const val PLAYBACK_TIME = "play_time"

            const val CAR_SCRATCHES_JPG = "Car_Scratches.jpg"

            const val CAR_SCRATCH_IMAGE_URL = "car_scratch_image_url"
            const val CAR_SCRATCH_IMAGE_NAME = "car_scratch_image_name"
            const val MCQ_LEVEL = "mcq_level"
            const val ALLOW_MAKE_PAYMENT = "allow_make_payment"
            const val SHOW_SPARES = "show_spared"
            const val PACKAGE_ID = "package_id"
            const val DEAL_ID = "deal_id"
            const val GM_NUM = "gm_num"
            const val ONLY_VERSION = "version"
            const val ACCESSORIES = "accessories"
            const val _ID = "id"
            const val ID_TYPE_PASSED = "id_type_passed"
            const val ENQUIRY_ID = "enquiry_id"
            const val IS_VIEW_ONLY = "is_view_only"
            const val FRAGMENT_TAG = "fragment_tag"
            const val SHOW_REMARK = "show_remarks"
            const val PURCHASE_INVOICE_KEY = "purchase_invoice"
            const val BID_ID = "bid_id"
            const val PHOTO_CLICK = "photo_click"
            const val TOTAL_AMOUNT = "total_amount"

            const val BID_TYPE = 1
            const val ENQUIRY_TYPE = 2

            const val PRIVATE_LABELS_ALLOWED_IN_ENQUIRY = "private_labels_allowed_in_enquiry"
            const val IS_BIDDING = "is_bidding"
            const val OPEN_ACTIVE_NEW_BIDS = "open_active_new_bids"
            const val DISCLAIMER = "disclaimer"
            const val DELTA_DATE = "delta_date"
            const val ACCEPTED_REJECTED = "accepted_rejected"
            const val ITEM_ID = "item_id"
            const val ITEM_NAME = "item_name"
            const val PREVIOUS_QUANTITY = "previous_quantity"
            const val PREVIOUS_PRICE = "previous_price"
            const val PRICE = "price"
            const val TOTAL_ITEM_QUANTITY = "total_item_quantity"
            const val ACCEPTED_ITEM_ID = "accepted_item_id"
            const val REJECTED_ITEM_ID = "rejected_item_id"
            const val NUMBER_OF_BIDS = "number_of_bids"
            const val GARAGE_NUMBER = "garage_number"
            const val GARAGE_LOCATION = "garage_location"
            const val GARAGE_ID = "garage_id"
            const val CART_VALUE = "cart_value"
            const val ITEM_QUANTITY = "item_quantity"
            const val TIME = "time"
            const val IS_ACCESSORIES = "is_accessories"
            const val TRIGGER_CONDITION = "trigger_condition"

            const val OPEN_RETAILER_HOMEPAGE = "open_retailer_home_page"
            const val OPEN_RETAILER_INVENTORY = "open_retailer_inventory"
            const val OPEN_RETAILER_PRIVATE_LABEL = "open_retailer_private_label"
            const val OPEN_RETAILER_WALLET = "open_retailer_home_wallet"
            const val OPEN_RETAILER_SETTLEMENT = "open_retailer_settlement"
            const val OPEN_RETAILER_SPARES_ANALYSIS = "open_retailer_spares_analysis"
            const val OPEN_RETAILER_NEW_ENQUIRY = "open_retailer_new_enquiry"
            const val OPEN_RETAILER_ACCEPTED_ENQUIRY = "open_retailer_accepted_enquiry"
            const val OPEN_RETAILER_WALLET_TRANSACTION_HISTORY =
                "open_retailer_wallet_transaction_history"

            val languageNamesMalaysia = arrayOf("English", "Bahasa Melayu")
            val languageCodesMalaysia = arrayOf("en", "ms")

            const val WARRANTY_NAME = "warranty_name"
            const val SERVICE_ID = "service_id"
            const val PARTIALLY_FILLED = "partially_filled"
            const val WARRANTY_PARTIALLY_FILLED = "warranty_health_card_partially_filled"

            const val OPEN_TRANSACTION_SUMMARY = "open_transaction_summary"
            const val PICKUP_REMAINING = "pickup_remaining"
            const val WALKIN_REMAINING = "walkin_remaining"

            const val WORK_IN_PROGRESS = "work_in_progress"
            const val READY_FOR_DELIVERY = "ready_for_delivery"
            const val DELIVERED = "delivered"
            const val DROPS = "drops"
            const val ORDER_LIST_TYPE = "order_list_type"
            const val ASSIGNED = "assigned"
            const val ON_TIME = "on_time"
            const val DELAYED = "delayed"
            const val TIME_SLOT = "time_slot"
            const val COUNT = "count"
            const val PREVIOUS_SELECTED_FILTER = "previous_selected_filter"
            const val NEW_SELECTED_FILTER = "new_selected_filter"
            const val DRIVER_NAME = "driver_name"
            const val NO_OF_DRIVERS = "no_of_drivers"
            const val PRIVATE_LABEL_ITEM_IDS = "private_label_item_ids"
            const val CAR_WISE_ITEM_IDS = "car_wise_item_ids"
            const val PRIVATE_LABEL_ITEM_QTY = "private_label_item_qty"
            const val CAR_WISE_ITEM_QTY = "car_wise_item_qty"
            const val IS_PRIVATE_LABEL = "is_private_label"
            const val PICKUP_DATE = "pickup_date"
            const val BATTERY_INFO = "battery_info"
            const val OTHER_REMARKS = "other_remarks"
            const val CAR_PARKED_AREA = "car_parked_area"
            const val CAR_DOCUMENTS = "car_documents"
            const val VEHICLE_PHYSICAL_STATUS = "vehicle_physical_status"
            const val FUEL_METER = "fuel_meter"
            const val POSITION = "position"
            const val JOBS = "jobs"
            const val FLAT = "flat"
            const val FINAL = "final"
            const val TOTAL = "total"
            const val ITEMISED = "itemised"
            const val COUPON = "coupon"
            const val TOTAL_DUE_AMOUNT = "total_due_amount"
            const val AMOUNT_PAID = "amount_paid"
            const val LEFT_AMOUNT = "left_amount"
            const val PAYMENT_STATUS = "payment_status"
            const val PAYMENT_MODE = "payment_mode"
            const val PARTIALLY_PAID = "partially_paid"
            const val FULLY_PAID = "fully_paid"
            const val DELIVER_CHECKS = "DELIVERY_CHECKS"
            const val OTP_STATUS = "otp_status"
            const val CAR_WISE_ITEM = "car_wise_item"
            const val SHOW_MARK_DELIVERED_OTP = "show_mark_delivered_otp"
            const val COUNTRY_PREFIX = "country_prefix"
            const val READ_ALOUD = "read_aloud"
            const val SOURCE = "source"
            const val TRAINING_ASSIGNED = "TRAINING_ASSIGNED"
            const val OPEN_HISTORY = "OPEN_HISTORY"

            const val SELECTED_BUCKET_ID = "selected_bucket_id"
            const val LEAD_TYPE = "lead_type"
            const val ORDERS_TO_PICKUP = "orders_to_pickup"
            const val WEB_URL = "web_url"
            const val WEB_TITLE = "web_title"
            const val IMAGE = "estimate_image"
            const val BANK_ACCOUNT_TYPE = "bank_account_type"

            const val SESSION_EXPIRED = "false"
            const val FROM_RETAILER_BID = "from_retailer_bid"
            const val VALUE = "value"
            const val SKU_ID = "sku_id"
            const val PAGES = "pages"
            const val OPEN_ORDER_DETAILS = "open_order_details"
            const val BUCKET_TYPE = "bucket_type"
            const val ENGINE_NO = "engine_no"
            const val CHASSIS_NO = "chassis_no"
            const val SHOW_MILES_DISCOUNT = "show_miles_discount"

            const val ORDER_INVENTORY_DROPDOWN = 1
            const val ORDER_REMARK_DROPDOWN = 2

            const val ORDER_DETAIL_LOCATION_CHECK_ENABLED = "order_detail_location_check_enabled"
            const val INVENTORY_LOCATION_CHECK_ENABLED = "inventory_location_check_enabled"
            const val SHOW_FOLLOWUP_UNANSWERED_MENU = "show_followup_unanswered_menu"
            const val ACCEPT_WITHIN_MINS = "accept_within_mins"
            const val SEARCH_ORDERS = "SEARCH ORDERS"
            const val SEARCH_TEXT = "search_text"
            const val SELECTED_LANGUAGE = "selected_language"

            const val PL_CATEGORY = "pl_category"
            const val PL_ORDER_TRACKING = "pl_order_tracking"
            const val TRANSACTION_SUMMARY_SCREEN = "transaction_summary_screen"
            const val SETTLEMENTS_SCREEN = "settlements_screen"
            const val WALLET_SCREEN = "wallet_screen"
            const val GST_HELP_AND_SUPPORT = "gst_support"
            const val NPS_SCREEN = "nps_screen"
            const val INVENTORY_SCREEN = "inventory_screen"
            const val PAYMENT_SCREEN = "payment_screen"
            const val OPEN_RETAILER_PAYMENT_SUMMARY = "open_retailer_payment_summary"
            const val TRANSMISSION_TYPE = "transmission_type"
            const val BRAKE_TYPE = "brake_type"

            const val ESTIMATE_IMAGES_PATH_PREFIX = "cr_app_data/estimate_images/"

            const val ESTIMATE_V2 = "estimate"

            const val SHOW_TUTORIAL = "show_tutorial"
            const val APP_ID = "app_id"
            const val IS_CAR_COLOR_SELECTION_MANDATORY = "is_car_color_selection_mandatory"
            const val AVAILABLE_COLORS = "available_colors"
            const val SELECTED_CAR_COLOR_ID = "selected_car_color_id"
            const val IS_BLOG = "is_blog"
            const val IS_TUTORIAL_VIDEO = "is_tutorial_video"
            const val BLOG_TYPE = "blog_type"
            const val BLOG_URL = "blog_url"
            const val BLOG_TITLE = "blog_title"
            const val MESSAGE = "message"
            const val FROM_RETAILER_LIST = "from_retailer_list"
            const val SERVICE_TYPE = "service_type"
            const val ACTION = "action"
            const val DISTANCE = "distance"
            const val STICKER = "sticker"
            const val SHOW_STICKER_SECTION = "show_sticker_section"
            const val IS_LUXURY = "is_luxury"

            const val BUCKET_LIST = "bucket_list"
            const val BUCKET_NAME = "bucket_name"
            const val SHOW_RESEND_OTP_BUTTON = "show_resend_otp_button"

        }
    }

}


