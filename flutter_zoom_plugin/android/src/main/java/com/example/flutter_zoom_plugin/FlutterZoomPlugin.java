package com.example.flutter_zoom_plugin;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterZoomPlugin */
public class FlutterZoomPlugin implements FlutterPlugin, MethodCallHandler,ActivityAware  {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  Activity activity;


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_zoom_plugin");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
      //joinMeeting(call, result);
    }
    else if (call.method.equals("joinMeeting")){
      joinMeeting(call, result);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
  public void joinMeeting(MethodCall methodCall, Result result)
  {
    ZoomSDK sdk = ZoomSDK.getInstance();
    ZoomSDKInitParams params = new ZoomSDKInitParams();
    params.appKey = methodCall.argument("appKey");
    params.appSecret = methodCall.argument("appSecret");
    params.domain = "zoom.us";
    params.enableLog = true;
    ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
      @Override
      public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) { }
      @Override
      public void onZoomAuthIdentityExpired() { }
    };
    sdk.initialize(context, listener, params);

    MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
    JoinMeetingOptions options = new JoinMeetingOptions();
    JoinMeetingParams params2 = new JoinMeetingParams();
    params2.displayName = "Dev"; // TODO: Enter your name
    params2.meetingNo = "82470883310";
    params2.password = "WEtVTVpDS1hJZkxuemp3cy9RRGdKZz09";
    meetingService.joinMeetingWithParams(context, params2, options);
    result.success(true);
  }
  @Override
  public void onDetachedFromActivityForConfigChanges() {
    this.activity = null;
  }

  @Override
  public void onDetachedFromActivity() {
    this.activity = null;
  }
}
