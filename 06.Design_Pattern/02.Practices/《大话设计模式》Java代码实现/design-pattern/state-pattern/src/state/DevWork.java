package state;

import state.State;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-18
 * @Description: 工作类（定义状态处理的上下文）
 */
public class DevWork {
    private State curState;
    private int curTime;

    public DevWork(State curState, int curTime) {
        this.curState = curState;
        this.curTime = curTime;
    }

    public int getCurTime() {
        return curTime;
    }

    public void setCurState(State curState) {
        this.curState = curState;
    }

    public void changeState(int curTime) {
        this.curTime = curTime;
        curState.handle(this);
    }
}
