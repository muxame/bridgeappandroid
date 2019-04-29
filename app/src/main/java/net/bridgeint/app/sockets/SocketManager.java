package net.bridgeint.app.sockets;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import net.bridgeint.app.application.Application;
import net.bridgeint.app.interfaces.SocketMessageListerner;
import net.bridgeint.app.models.ChatModel.Message;
import net.bridgeint.app.network.ServerURLs;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import org.json.JSONObject;

import java.util.Locale;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.client.SocketIOException;
import io.socket.emitter.Emitter;

/**
 * Created by DeviceBee on 8/22/2017.
 */

public class SocketManager {
    private static Socket mSocket;
    private static SocketManager mInstance;
    private Context mContext;
    private static String TAG = "Socket";
    SocketMessageListerner listerner;


    public SocketManager(final Context mContext, SocketMessageListerner listerner) {
        this.mContext = mContext;
        this.listerner = listerner;
        mInstance = this;
    }

    public void socketConnect() {
        if (mSocket == null)
            connectSocketManager();

        if (!mSocket.connected())
            mSocket.connect();
    }

    public void removeListner() {
        listerner=null;
    }

    public boolean isSocketConnect() {
        return mSocket != null && mSocket.connected();
    }
    public void connectSocketManager() {

        try {
            String query = String.format(Locale.getDefault(),
                    ServerURLs.SERVER_URL,
                    SharedClass.userModel.getAccessKey(),
                    SharedClass.userModel.getId().toString()
            );
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            opts.secure = false;
            opts.timeout = 7000;
            opts.reconnectionDelay = 1000;
            opts.query = query;
            opts.transports = new String[]{"polling"};
            mSocket = IO.socket(ServerURLs.CHAT_SERVER_URL_WITH_PORT, opts);



            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Event Connect");
                }
            });

            /************************************************************************************/

            // Socket Connected Event
            mSocket.on(Constants.SOCKET_EVENT_CONNECTED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "Socket Event Connect");
                }
            });

            // Get New Message EVENT
            mSocket.on(Constants.SOCKET_EVENT_SEND_MESSAGE_SERVER, new Emitter.Listener() {
                @Override
                public void call(Object... data) {
                    if (data.length > 0) {
                        try {
                            JSONObject dict = (JSONObject) data[0];
                            boolean error = false;
                            if (dict.has(Constants.ERROR)) {
                                error = dict.getBoolean(Constants.ERROR);
                            }
                            if (!error) {
                                if (dict.has(Constants.DATA)) {
                                    Message message = new Message(dict.getJSONObject(Constants.DATA));
                                    if (!Application.isShowChat()) {
                                        Handler handler = new Handler(Looper.getMainLooper());

                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                Activity activity = Application.getAppInstance().getCurrentActivity();
                                                Utility.showChatDialog(activity, message.getMessage());
                                            }
                                        }, 1000);

                                    }
                                    if(null!=listerner) {
                                        listerner.onGetMessageAck(message, Constants.RECIEVE_NEW_MESSAG);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Admin ONLINE Event Listener
            mSocket.on(Constants.SOCKET_EVENT_ADMIN_LIVE, new Emitter.Listener() {
                @Override
                public void call(Object... data) {
                    if(null!=listerner) {
                        listerner.onGetMessageAck(null, Constants.ADMIN_ONLINE);
                    }
                }
            });
            // Admin Recived MSG event
            mSocket.on(Constants.SOCKET_EVENT_SERVER_READ, new Emitter.Listener() {
                @Override
                public void call(Object... data) {
                    if(null!=listerner) {
                        listerner.onGetMessageAck(null, Constants.ADMIN_READ_MSG);
                    }
                }
            });
            // Admin OFFLINE Event Listener
            mSocket.on(Constants.SOCKET_EVENT_ADMIN_OFFLINE, new Emitter.Listener() {
                @Override
                public void call(Object... data) {
                    if(null!=listerner) {
                        listerner.onGetMessageAck(null, Constants.ADMIN_OFFLINE);
                    }
                }
            });

            // Admin Typing Event Listener
            mSocket.on(Constants.SOCKET_EVENT_STATE_TYPING_SERVER, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if(null!=listerner) {
                       listerner.onGetMessageAck(null, Constants.ADMIN_TYPING);
                    }
                }
            });

            // Socket Event Disconnect
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG,"socket disconnect");
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "eventConnectError");
                    for (Object o : args) {
                        Log.d("TAG", "object: " + o.toString());
                        if (o instanceof SocketIOException)
                            ((SocketIOException) o).printStackTrace();
                    }

                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "eventError");
                }
            });

            if (!mSocket.connected())
                mSocket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void socketDisconnect() {
        if (mSocket != null)
            mSocket.disconnect();
    }

    public void SendMessage(String accessKey, final String message, String toUser, String fromUser, String tempId, String type, final SocketMessageListerner listerner) {
        if (mSocket == null)
            return;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(Constants.ACCESS_KEY, accessKey);
            jsonObject.put(Constants.MESSAGE, message);
            jsonObject.put(Constants.TO_USER, toUser);
            jsonObject.put(Constants.FROM_USER, fromUser);
            jsonObject.put(Constants.TEMP_ID, tempId);
            jsonObject.put(Constants.DURATION, 0);
            jsonObject.put(Constants.TYPE, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Message Sent Event
        mSocket.emit(Constants.SOCKET_EVENT_SEND_MESSAGE, jsonObject.toString(), new Ack() {
            @Override
            public void call(Object... data) {
                if (data.length > 0) {
                    try {
                        JSONObject dict = (JSONObject) data[0];
                        boolean error = false;
                        if (dict.has(Constants.ERROR)) {
                            error = dict.getBoolean(Constants.ERROR);
                        }
                        if (!error) {
                            if (dict.has(Constants.DATA)) {
                                Message message = new Message(dict.getJSONObject(Constants.DATA));
                                listerner.onGetMessageAck(message, Constants.MESSAGE_UPDATE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void startTyping(String accessKey, String toUser, String fromUser) {
        if (mSocket == null)
            return;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(Constants.ACCESS_KEY, accessKey);
            jsonObject.put(Constants.TO_USER, toUser);
            jsonObject.put(Constants.FROM_USER, fromUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSocket.emit(Constants.SOCKET_EVENT_STATE_TYPING, jsonObject.toString(), new Ack() {
            @Override
            public void call(Object... data) {
                if (data.length > 0) {
                    try {
                        JSONObject dict = (JSONObject) data[0];
                        Log.d(TAG, dict.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendAckOnRecievingMessage(String accessKey, String toUser, String fromUser, String messageId) {
        if (mSocket == null)
            return;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(Constants.ACCESS_KEY, accessKey);
            jsonObject.put(Constants.TO_USER, toUser);
            jsonObject.put(Constants.FROM_USER, fromUser);
            jsonObject.put(Constants.MESSAGE_ID, messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSocket.emit(Constants.SOCKET_EVENT_MESSAGE_RECEIVED, jsonObject.toString(), new Ack() {
            @Override
            public void call(Object... data) {
                if (data.length > 0) {
                    try {
                        JSONObject dict = (JSONObject) data[0];
                        Log.d(TAG, dict.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
