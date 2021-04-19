package com.example.mars.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardNonce;
import com.braintreepayments.api.models.ClientToken;
import com.braintreepayments.api.models.GooglePaymentCardNonce;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.ThreeDSecureAdditionalInformation;
import com.braintreepayments.api.models.ThreeDSecurePostalAddress;
import com.braintreepayments.api.models.ThreeDSecureRequest;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.example.mars.R;
import com.example.mars.data.CourseData;
import com.example.mars.database.DatabaseHandler;
import com.example.mars.util.Settings;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivityPayment extends AppCompatActivity implements PaymentMethodNonceCreatedListener,
        BraintreeCancelListener, BraintreeErrorListener, DropInResult.DropInResultListener {
    private static final int DROP_IN_REQUEST = 100;

    private static final String KEY_NONCE = "nonce";
    TextView tv_course_name, tv_course_id, tv_fee, tv_total_amout;
    DatabaseHandler databaseHandler;
    String mAuthorization = "sandbox_tvd88k39_qcy7k7y59njv599v";
    private PaymentMethodType mPaymentMethodType;
    private PaymentMethodNonce mNonce;
    private CardView mPaymentMethod;
//        private TextView mNonceString;
//        private TextView mNonceDetails;
//        private TextView mDeviceData;
    private ImageView mPaymentMethodIcon;
    private TextView mPaymentMethodTitle;
    private TextView mPaymentMethodDescription;
    private Button mAddPaymentMethodButton;
    private Button mPurchaseButton;
    private ProgressDialog mLoading;
    private boolean mShouldMakePurchase = false;
    private boolean mPurchased = false;
    private ImageView btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_test);

        mPaymentMethod = findViewById(R.id.payment_method);
        mPaymentMethodIcon = findViewById(R.id.payment_method_icon);
        mPaymentMethodTitle = findViewById(R.id.payment_method_title);
        mPaymentMethodDescription = findViewById(R.id.payment_method_description);
        tv_course_name = findViewById(R.id.tv_course_name);
        tv_course_id = findViewById(R.id.tv_course_id);
        tv_fee = findViewById(R.id.tv_fee);
        tv_total_amout = findViewById(R.id.tv_total_amout);
        btn_back = findViewById(R.id.btn_back);

        databaseHandler = new DatabaseHandler(this);
        tv_course_name.setText("Course Name:- "+getIntent().getStringExtra("name"));
        tv_course_id.setText("Course Id:- "+String.valueOf(getIntent().getIntExtra("id", 0)));
        tv_fee.setText("Course Fees:- "+getIntent().getStringExtra("fees"));
        int fee = Integer.parseInt(getIntent().getStringExtra("fees")) + 10;
        tv_total_amout.setText("Total Amount:- $" + fee);

        mAddPaymentMethodButton = findViewById(R.id.add_payment_method);
        mPurchaseButton = findViewById(R.id.purchase);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_NONCE)) {
                mNonce = savedInstanceState.getParcelable(KEY_NONCE);
            }
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPurchased) {
            mPurchased = false;
            clearNonce();

            try {
                if (ClientToken.fromString(mAuthorization) instanceof ClientToken) {
                    DropInResult.fetchDropInResult(this, mAuthorization, this);
                } else {
                    mAddPaymentMethodButton.setVisibility(VISIBLE);
                }
            } catch (InvalidArgumentException e) {
                mAddPaymentMethodButton.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNonce != null) {
            outState.putParcelable(KEY_NONCE, mNonce);
        }
    }

    public void launchDropIn(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(mAuthorization)
                .requestThreeDSecureVerification(Settings.isThreeDSecureEnabled(this))
                .collectDeviceData(Settings.shouldCollectDeviceData(this))
                .googlePaymentRequest(getGooglePaymentRequest())
                .maskCardNumber(true)
                .maskSecurityCode(true)
                .allowVaultCardOverride(Settings.isSaveCardCheckBoxVisible(this))
                .vaultCard(Settings.defaultVaultSetting(this))
                .vaultManager(Settings.isVaultManagerEnabled(this))
                .cardholderNameStatus(Settings.getCardholderNameStatus(this));
        if (Settings.isThreeDSecureEnabled(this)) {
            dropInRequest.threeDSecureRequest(demoThreeDSecureRequest());
        }

        startActivityForResult(dropInRequest.getIntent(this), DROP_IN_REQUEST);
    }

    private ThreeDSecureRequest demoThreeDSecureRequest() {
        ThreeDSecurePostalAddress billingAddress = new ThreeDSecurePostalAddress()
                .givenName("Jill")
                .surname("Doe")
                .phoneNumber("5551234567")
                .streetAddress("555 Smith St")
                .extendedAddress("#2")
                .locality("Chicago")
                .region("IL")
                .postalCode("12345")
                .countryCodeAlpha2("US");

        ThreeDSecureAdditionalInformation additionalInformation = new ThreeDSecureAdditionalInformation()
                .accountId("account-id");

        ThreeDSecureRequest threeDSecureRequest = new ThreeDSecureRequest()
                .amount("1.00")
                .versionRequested(Settings.getThreeDSecureVersion(this))
                .email("test@email.com")
                .mobilePhoneNumber("3125551234")
                .billingAddress(billingAddress)
                .additionalInformation(additionalInformation);

        return threeDSecureRequest;
    }

    public void purchase(View v) {

        showDialog("Item purchased successfully!!");

        mPurchased = true;
    }

    @Override
    public void onResult(DropInResult result) {
        if (result.getPaymentMethodType() == null) {
            mAddPaymentMethodButton.setVisibility(VISIBLE);
        } else {
            mAddPaymentMethodButton.setVisibility(GONE);

            mPaymentMethodType = result.getPaymentMethodType();

            mPaymentMethodIcon.setImageResource(result.getPaymentMethodType().getDrawable());
            if (result.getPaymentMethodNonce() != null) {
                displayResult(result.getPaymentMethodNonce(), result.getDeviceData());
            }

            mPurchaseButton.setEnabled(true);
        }
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//        super.onPaymentMethodNonceCreated(paymentMethodNonce);
        displayResult(paymentMethodNonce, null);
        safelyCloseLoadingView();

        if (mShouldMakePurchase) {
            purchase(null);
        }
    }

    @Override
    public void onCancel(int requestCode) {

        safelyCloseLoadingView();

        mShouldMakePurchase = false;
    }

    @Override
    public void onError(Exception error) {


        safelyCloseLoadingView();

        mShouldMakePurchase = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        safelyCloseLoadingView();

        if (resultCode == RESULT_OK) {
            DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
            displayResult(result.getPaymentMethodNonce(), result.getDeviceData());
            mPurchaseButton.setEnabled(true);
        } else if (resultCode != RESULT_CANCELED) {
            safelyCloseLoadingView();
//                showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR))
//                        .getMessage());
        }
    }


    protected void reset() {
        mPurchaseButton.setEnabled(false);

        mAddPaymentMethodButton.setVisibility(GONE);

        clearNonce();
    }


    private void displayResult(PaymentMethodNonce paymentMethodNonce, String deviceData) {
        mNonce = paymentMethodNonce;
        mPaymentMethodType = PaymentMethodType.forType(mNonce);

        mPaymentMethodIcon.setImageResource(PaymentMethodType.forType(mNonce).getDrawable());
        mPaymentMethodTitle.setText(paymentMethodNonce.getTypeLabel());
        mPaymentMethodDescription.setText(paymentMethodNonce.getDescription());
        mPaymentMethod.setVisibility(VISIBLE);

//            mNonceString.setText("NOnence" + ": " + mNonce.getNonce());
//            mNonceString.setVisibility(VISIBLE);

        String details = "";
        if (mNonce instanceof CardNonce) {
            CardNonce cardNonce = (CardNonce) mNonce;

            details = "Card Last Two: " + cardNonce.getLastTwo() + "\n";
            details += "3DS isLiabilityShifted: " + cardNonce.getThreeDSecureInfo().isLiabilityShifted() + "\n";
            details += "3DS isLiabilityShiftPossible: " + cardNonce.getThreeDSecureInfo().isLiabilityShiftPossible();
        } else if (mNonce instanceof PayPalAccountNonce) {
            PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) mNonce;

            details = "First name: " + paypalAccountNonce.getFirstName() + "\n";
            details += "Last name: " + paypalAccountNonce.getLastName() + "\n";
            details += "Email: " + paypalAccountNonce.getEmail() + "\n";
            details += "Phone: " + paypalAccountNonce.getPhone() + "\n";
            details += "Payer id: " + paypalAccountNonce.getPayerId() + "\n";
            details += "Client metadata id: " + paypalAccountNonce.getClientMetadataId() + "\n";
            details += "Billing address: " + "sdsdsdsdsd" + "\n";
            details += "Shipping address: " + "formatAddress(paypalAccountNonce.getShippingAddress()";
        } else if (mNonce instanceof VenmoAccountNonce) {
            VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) mNonce;

            details = "Username: " + venmoAccountNonce.getUsername();
        } else if (mNonce instanceof GooglePaymentCardNonce) {
            GooglePaymentCardNonce googlePaymentCardNonce = (GooglePaymentCardNonce) mNonce;

            details = "Underlying Card Last Two: " + googlePaymentCardNonce.getLastTwo() + "\n";
            details += "Email: " + googlePaymentCardNonce.getEmail() + "\n";
            details += "Billing address: " + "formatAddress(googlePaymentCardNonce.getBillingAddress()) " + "\n";
            details += "Shipping address: " + "formatAddress(googlePaymentCardNonce.getShippingAddress())";
        }


        mAddPaymentMethodButton.setVisibility(GONE);
        mPurchaseButton.setEnabled(true);
    }

    private void clearNonce() {
        mPaymentMethod.setVisibility(GONE);

        mPurchaseButton.setEnabled(false);
    }


    private GooglePaymentRequest getGooglePaymentRequest() {
        return new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setTotalPrice("1.00")
                        .setCurrencyCode("USD")
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .build())
                .emailRequired(true);
    }

    private void safelyCloseLoadingView() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    protected void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message).setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CourseData courseData=new CourseData(getIntent().getIntExtra("id",0),getIntent().getStringExtra("name"),true,"Purchased");
                        databaseHandler.updateCourseData(courseData);

                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}
