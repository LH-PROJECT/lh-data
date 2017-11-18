package com.unitedratings.lhcrm.core;

/**
 * @author wangyongxin
 */
public abstract class LoopThread extends Thread{
    private boolean exitFlag = false;

    abstract boolean init();

    abstract void unInit();

    abstract void work();

    @Override
    public void run() {
        if(init()){
            for (;;){
                work();
                if(exitFlag){
                    break;
                }
            }
        }
        unInit();
    }

    public boolean isExitFlag() {
        return exitFlag;
    }

    public void setExitFlag(boolean exitFlag) {
        this.exitFlag = exitFlag;
    }
}
