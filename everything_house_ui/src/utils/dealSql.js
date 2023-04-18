export default {
  methods: {
    // 这个函数是是否需要删除交互使用的,
    dealConfirmDelete(message, callback) {
      this.$confirm(message, '提示', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消删除',
        type: 'warning'
      }).then(() => {
        callback()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    toggleLoading(loading) {
      let loadingInstance;

      if (loading) {
        loadingInstance = this.$loading({
          lock: true,
          text: "正在加载中...",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
      } else {
        loadingInstance && loadingInstance.close();
      }

      return loadingInstance;
    },
  },
}
