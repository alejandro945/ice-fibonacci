module Demo
{
    interface CallbackReceiver
    {
        void callback(string message);
    } 

    interface CallbackSender
    {
        void initiateCallback(CallbackReceiver* proxy, string message);
    }
}