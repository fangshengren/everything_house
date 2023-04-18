export default {
  methods: {
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
    }
  }
}
