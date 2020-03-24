//package com.example.tag;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.wifi.p2p.WifiP2pDevice;
//import android.net.wifi.p2p.WifiP2pManager;
//
///**
// * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
// */
//public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
//
//    private WifiP2pManager mManager;
//    private WifiP2pManager.Channel mChannel;
//    private RegisterActivity mActivity;
//
//    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
//                                       RegisterActivity activity) {
//        super();
//        this.mManager = manager;
//        this.mChannel = channel;
//        this.mActivity = activity;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//
//        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
//            // Check to see if Wi-Fi is enabled and notify appropriate activity
//            // Determine if Wifi P2P mode is enabled or not, alert
//            // the Activity.
//            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
//            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
//                mActivity.setIsWifiP2pEnabled(true);
//            } else {
//                mActivity.setIsWifiP2pEnabled(false);
//            }
//        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
//            // Call WifiP2pManager.requestPeers() to get a list of current peers
//        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
//            // Respond to new connection or disconnections
//        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            // Respond to this device's wifi state changing
////            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
////                    .findFragmentById(R.id.frag_list);
////            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
////                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
//        }
//    }
//}