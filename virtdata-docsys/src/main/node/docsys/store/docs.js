
// https://www.mikestreety.co.uk/blog/vue-js-using-localstorage-with-the-vuex-store
export const state = () => ({
    isDrawerOpen: true,
    isMenuLocked: false
});

export const mutations = {
    toggleDrawerState(state, newDrawerState) {
        if (state.isMenuLocked) {
            return;
        }
        state.isDrawerOpen=!state.isDrawerOpen;
    },
    setDrawer(state, newDrawerState) {
        if (state.isMenuLocked) {
            return;
        }
        state.isDrawerOpen=newDrawerState;
    },
    setMenuLock(state, newLockState) {
        state.isMenuLocked=newLockState;
    }
};