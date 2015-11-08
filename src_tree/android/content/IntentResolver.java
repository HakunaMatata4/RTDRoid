package android.content;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import android.app.ActivityDelegator;
import android.app.ServiceDelegator;


public class IntentResolver {
    private static IntentResolver instance;
    private Map<String, ActivityDelegator> activityList;
    private Map<String, ServiceDelegator> serviceList;
    private Map<String, Integer> bindingRecord;
    private Map<String, Set<String>> reversedBindingRecord;
    private Map<String, BroadcastReceiver> receiverList;
    private Map<String, Set<BroadcastReceiver>> actionToReceiver;

    private IntentResolver() {
        this.activityList = new TreeMap<String, ActivityDelegator>();
        this.serviceList = new TreeMap<String, ServiceDelegator>();
        this.bindingRecord = new TreeMap<String, Integer>();
        this.reversedBindingRecord = new TreeMap<String, Set<String>>();
        this.receiverList = new TreeMap<String, BroadcastReceiver>();
        this.actionToReceiver = new TreeMap<String, Set<BroadcastReceiver>>();
        
    }

    public static IntentResolver getInstance() {
        if (instance == null) {
            instance = new IntentResolver();
        }
        return instance;
    }

    public void registerActivity(String name, ActivityDelegator delegator) {
        activityList.put(name, delegator);
    }

    public void registerService(String name, ServiceDelegator delegator) {
        serviceList.put(name, delegator);
    }

    public void unregisterActivity(String name) {
        activityList.remove(name);
    }

    public void unregisterService(String name) {
        serviceList.remove(name);
    }

    public ServiceDelegator getServiceDelegator(String name) {
        return serviceList.get(name);
    }

    public ActivityDelegator getActivityDelegator(String name) {
        return activityList.get(name);
    }
    public void addToReceiverList(String name, BroadcastReceiver reciever){
        receiverList.put(name, reciever);
    }
    
    public void removeFromReceiverList(String name){
        receiverList.remove(name);
    }
    
    public void addActionReceiver(String name, BroadcastReceiver r){
        if( actionToReceiver.containsKey(name) ){
            actionToReceiver.get(name).add(r);
        }else{
            Set<BroadcastReceiver> newRecord = new TreeSet<BroadcastReceiver>();
            newRecord.add(r);
            actionToReceiver.put(name, newRecord);
        }
    }
    
    public BroadcastReceiver getReceiverByName(String name){
        return receiverList.get(name);
    }
    
    public Set<BroadcastReceiver> getReceiverByAction(String action){
        return actionToReceiver.get(action);
    }
    
    public void addBindingRecord(String bindedComponent, String bindingComponent){
        if( bindingRecord.containsKey(bindedComponent) ){
            int count = bindingRecord.get(bindedComponent)+1;
            bindingRecord.put(bindedComponent, count);
        }else{
            bindingRecord.put(bindedComponent, 1);
        }
        
        if(reversedBindingRecord.containsKey(bindingComponent)){
            reversedBindingRecord.get(bindingComponent).add(bindedComponent);
        }else{
            Set<String> set = new TreeSet<String>();
            set.add(bindedComponent);
            reversedBindingRecord.put(bindingComponent, set);
        }
    }
    
    public Set<String> removeBindingRecord(String bindingComponent){
         if( reversedBindingRecord.containsKey(bindingComponent) ){
             Set<String> set = reversedBindingRecord.get(bindingComponent);
             reversedBindingRecord.remove(bindingComponent);
             return set;
         }else{
             return null;
         }
    }
    
    public void updateBindingRecord(String bindedComponent, int count){
        bindingRecord.put(bindedComponent, count);
    }
    
    public int getBindingRecord(String bindedComponent){
        return bindingRecord.get(bindedComponent);
    }
    
    public void removeBindingRecord(String bindedComponent, int count){
        bindingRecord.remove(bindedComponent);
    }
}