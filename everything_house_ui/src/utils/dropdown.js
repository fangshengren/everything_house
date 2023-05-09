export default {
    bind: function (el, binding, vnode) {
        const dropdown = vnode.componentInstance;
        el.addEventListener("mouseenter", () => {
            dropdown.visible = true;
        });
        el.addEventListener("mouseleave", () => {
            dropdown.visible = false;
        });
    },
};
