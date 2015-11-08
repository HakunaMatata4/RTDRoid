
package android.content;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.realtime.LTMemory;
import javax.realtime.ScopedMemory;

import com.fiji.rtdroid.ActivityConfiguration;
import com.fiji.rtdroid.AndroidApplication;
import com.fiji.rtdroid.ServiceConfiguration;

import edu.buffalo.rtdroid.SystemConfig;
import android.app.ScopedActivityDelegator;
import android.app.ScopedServiceDelegator;

public class ScopedIntentResolver {
    private static ScopedIntentResolver instance;
    private Map<String, LTMemory> activityList;
    private Map<String, LTMemory> serviceList;
    private Map<String, LTMemory> receiverList;
    private Map<String, String> memoryAreaToService;
    private Map<String, String> memoryAreaToActivity;
    private Map<String, Set<LTMemory>> actionToReceiver;
    private AndroidApplication appConfig;
    private Map<String, Integer> bindingRecord;
    private Map<String, Set<String>> reversedBindingRecord;

    private ScopedIntentResolver() {
        this.activityList = new TreeMap<String, LTMemory>();
        this.serviceList = new TreeMap<String, LTMemory>();
        this.memoryAreaToService = new TreeMap<String, String>();
        this.memoryAreaToActivity = new TreeMap<String, String>();
        this.bindingRecord = new TreeMap<String, Integer>();
        this.reversedBindingRecord = new TreeMap<String, Set<String>>();
        this.receiverList = new TreeMap<String, LTMemory>();
        this.actionToReceiver = new TreeMap<String, Set<LTMemory>>();
    }

    public static ScopedIntentResolver getInstance() {
        if (instance == null) {
            instance = new ScopedIntentResolver();
        }
        return instance;
    }

    public ScopedMemory registerActivity(final ActivityConfiguration config) {
        final LTMemory area = new LTMemory(SystemConfig.SERVICE_SCOPE_SIZE);
        area.enter(new Runnable() {

            public void run() {
                ScopedActivityDelegator delegator = new ScopedActivityDelegator(appConfig, config, area);
                area.setPortal(delegator);
            }
        });
        activityList.put(config.getName(), area);
        memoryAreaToActivity.put(area.toString(), config.getName());
        return area;
    }

    public ScopedMemory registerService(final ServiceConfiguration config) {
        final LTMemory area = new LTMemory(SystemConfig.SERVICE_SCOPE_SIZE);
        area.enter(new Runnable() {
            public void run() {
                ScopedServiceDelegator delegator = new ScopedServiceDelegator(appConfig, config, area);
                area.setPortal(delegator);
            }
        });
        memoryAreaToService.put(area.toString(), config.getName());
        serviceList.put(config.getName(), area);
        return area;
    }
    
    public void addToReceiverList(String name, LTMemory area){
        receiverList.put(name, area);
    }
    
    public void removeFromReceiverList(String name){
        receiverList.remove(name);
    }
    
    public void addActionReceiver(String name, LTMemory area){
        if( actionToReceiver.containsKey(name) ){
            actionToReceiver.get(name).add(area);
        }else{
            Set<LTMemory> newRecord = new TreeSet<LTMemory>();
            newRecord.add(area);
            actionToReceiver.put(name, newRecord);
        }
    }
    
    public LTMemory getReceiverByName(String name){
        return receiverList.get(name);
    }
    
    public Set<LTMemory> getReceiverByAction(String action){
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
    
    public void dump(){
        //activityList.entrySet()
        for (Entry<String, LTMemory> entry: activityList.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        for (Entry<String, LTMemory> entry: serviceList.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        for (Entry<String, Integer> entry: bindingRecord.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void unregisterActivity(String name) {
        activityList.remove(name);
    }

    public void unregisterService(String name) {
        serviceList.remove(name);
    }

    public ScopedMemory getServiceScope(String name) {
        return serviceList.get(name);
    }

    public ScopedMemory getActivityScope(String name) {
        return activityList.get(name);
    }

    public void setAndroidConfiguration(AndroidApplication config) {
        this.appConfig = config;
    }
}
