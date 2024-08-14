<template>
  <div class="user-container">
    <div class="filter-container">
      <!-- 用户搜索表单 -->
      <el-form
        :model="tableData"
        size="small"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="用户名称">
          <el-input
            v-model="tableData.userName"
            placeholder="请输入用户名称"
            clearable
            @keyup.enter.native="getUserList"
          />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="tableData.minCreateTime"
            class="date-picker"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="起始日期"
          />
          <el-date-picker
            v-model="tableData.maxCreateTime"
            class="date-picker"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="截止日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="getUserList"
            >Search</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >Reset</el-button
          >
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            @click="handleAdd"
          >
            Add
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="handleBatchDelete"
          >
            Delete</el-button
          >
        </el-form-item>
      </el-form>

      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        Export
      </el-button>
      <el-checkbox v-model="showReviewer" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">
        reviewer
      </el-checkbox> -->
    </div>
    <!-- <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="showTableList.slice(0, 10)"
      border
      fit
      sortable
      highlight-current-row
      style="width: 100%"
      height="800"
      :row-style="{ height: '100px' }"
    > -->
    <!-- 用户列表 -->
    <el-table
      :data="tableData.list"
      v-loading="listLoading"
      @selection-change="(val) => (tableData.selection = val)"
      @sort-change="handleSortChange"
    >
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
      <el-table-column width="50">
        <template slot-scope="scope">
          <img :id="'avatar-' + scope.row.id" class="table-avatar" />
        </template>
      </el-table-column>
      <el-table-column prop="user_name" label="用户名" sortable="custom" />
      <el-table-column prop="true_name" label="真实姓名" sortable="custom" />
      <el-table-column prop="role_list" label="角色" sortable="custom" />
      <el-table-column prop="create_time" label="创建时间" sortable="custom" />
      <el-table-column
        prop="status"
        label="是否激活"
        sortable="custom"
        width="100"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleSwitch(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit" plain
            @click="handleEdit(scope.row)"
          >
            Edit
          </el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete" plain
            @click="handleDelete([scope.row.id])"
          >
            Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      class="pagination"
      :current-page.sync="tableData.pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size.sync="tableData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.total"
      @size-change="getUserList"
      @current-change="getUserList"
    />
    <!-- 操作列
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
            Edit
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
            Delete
          </el-button>
        </template>
      </el-table-column> -->

    <!-- <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" /> -->

    <!-- add / edit 对话框 -->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialogFormVisible"
    >
      <el-form
        ref="addDataForm"
        :rules="addRules"
        :model="addTemp"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left: 50px"
      >
        <el-form-item
          v-for="column in filteredColumns"
          :key="column"
          :label="column"
        >
          <el-input v-model="addTemp[column]"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false"> Cancel </el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'add' ? addData() : editData()"
        >
          Confirm
        </el-button>
      </div>
    </el-dialog> -->

    <!-- 用户编辑/创建窗口 -->
    <el-dialog
      class="user-edit-dialog"
      :title="textMap[dialogStatus]"
      :visible.sync="addDialogFormVisible"
      width="50%"
      top="8vh"
    >
      <el-form
        ref="userEditForm"
        status-icon
        :model="userEditForm"
        label-width="80px"
        :rules="userEditForm.id ? userUpdateRules : userCreateRules"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userEditForm.userName" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="userEditForm.trueName" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userEditForm.password" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userEditForm.email" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userEditForm.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="userEditForm.address" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="userEditForm.introduction" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="userEditForm.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="userEditForm.roleIds"
            multiple
            placeholder="请选择角色"
          >
            <el-option
              v-for="role in allRoles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'add' ? addData() : editData()"
        >
          确定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import axios from "axios";

export default {
  name: "User",
  components: { Pagination },

  computed: {
    ...mapGetters(["name"]),
  },
  data() {
    return {
      //表格数据
      tableData: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 10,
        userName: "",
        minCreateTime: "",
        maxCreateTime: "",
        orderBy: "",
        orderMethod: "asc",
        selection: [],
      },

      //筛选条件
      listQuery: {
        tableName: "",
      },
      showTableOptions: [],
      showTableName: "",
      showTableColumns: [],
      showTableList: [],

      tableKey: 0,
      listLoading: true,
      total: 0,

      //add edit  Dialog
      userEditForm: {
        id: "",
        userName: "",
        trueName: "",
        password: "",
        email: "",
        gender: "",
        address: "",
        introduction: "",
        phone: "",
        roleIds: [],
      },
      userCreateRules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            validator: this.userNameValidator,
          },
        ],
        password: [
          {
            required: true,
            trigger: "change",
            validator: this.passwordValidator,
          },
        ],
        roleIds: [
          { required: true, trigger: "change", validator: this.roleValidator },
        ],
      },
      userUpdateRules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            validator: this.userNameValidator,
          },
        ],
        password: [{ trigger: "change", validator: this.passwordValidator }],
        roleIds: [
          { required: true, trigger: "change", validator: this.roleValidator },
        ],
      },
      currentEditRow: {},
      allRoles: [],

      //dialog title
      addDialogFormVisible: false,
      dialogStatus: "", // 'add' or 'edit'
      textMap: {
        add: "新增用户",
        edit: "修改用户信息",
      },
      addTemp: {},
      addRules: {},
      addColumns: [],

      editId: "",
    };
  },
  mounted() {
    this.getAllRoles()
    this.getUserList()
  },
  computed: {
    filteredColumns() {
      return this.showTableColumns.filter((column) => column !== "id");
    },
  },
  methods: {
    //验证
    userNameValidator(rule, value, callback) {
      if (!value) {
        callback(new Error("请输入用户名"));
      } else if (
        this.userEditForm.id &&
        value === this.currentEditRow.userName
      ) {
        callback();
      } else {
        checkUserName(value).then((res) => {
          callback(res.data.data ? new Error("用户名已存在") : undefined);
        });
      }
    },
    passwordValidator(rule, value, callback) {
      if (!value && this.userEditForm.id) {
        callback();
      } else if (!value || value.length < 6) {
        callback(new Error("密码长度不能小于6位"));
      } else {
        callback();
      }
    },
    roleValidator(rule, value, callback) {
      if (!value || value.length === 0) {
        callback(new Error("角色不能为空"));
      } else {
        callback();
      }
    },
    resetQuery() {
      this.tableData.userName = "";
      this.tableData.minCreateTime = "";
      this.tableData.maxCreateTime = "";
    },


    //方法
    getAllRoles() {
      axios
        .get(`/api/roles/getRoles`)
      .then(res => {
        this.allRoles = res.data.data
      })
    },


    getUserList() {
      this.listLoading = true;

      console.log("waiting...");
      const params = {
        searchContent: this.tableData.userName,
        pageNum: this.tableData.pageNum, // 使用当前的 pageNum
        pageSize: this.tableData.pageSize, // 使用当前的 pageSize
      };
      axios
        .get(`/api/users/getUserList`, { params })
        .then((response) => {
          this.tableData.list = response.data.data;
          this.listLoading = false;
        })
        .catch((error) => {
          console.error("There was an error fetching the table data:", error);
          this.listLoading = false;
        });
    },
    handleSwitch(row) {
      UserApi.changeUserStatus(row.id, row.status).then(() => {
        this.$message.success("操作成功");
      });
    },
    handleSortChange({ column, prop, order }) {
      this.tableData.orderBy = prop;
      this.tableData.orderMethod = order === "ascending" ? "asc" : "desc";
      this.getUserList();
    },

    resetTemp() {
      //重置dialog
      for (const key in this.userEditForm) {
        this.userEditForm[key] = "";
      }
      this.userEditForm.roleIds = [];
    },

    handleAdd() {
      this.resetTemp(); // 重置表单数据
      this.dialogStatus = "add";
      this.addDialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["userEditForm"].clearValidate(); // 清空表单验证状态
      });
    },
    addData() {
    this.$refs.userEditForm.validate((valid) => {
      if (valid) {
        // 如果表单验证通过
        const params = {
          userName: this.userEditForm.userName,
          trueName: this.userEditForm.trueName,
          password: this.userEditForm.password,
          email: this.userEditForm.email,
          gender: this.userEditForm.gender,
          address: this.userEditForm.address,
          introduction: this.userEditForm.introduction,
          phone: this.userEditForm.phone,
          roleIds: this.userEditForm.roleIds,
        };

        axios
          .post('/api/users/addUser',  { params })
          .then((response) => {
            if (response.data.success) {
              this.$message({
                message: '用户添加成功',
                type: 'success',
              });
              this.addDialogFormVisible = false; // 关闭对话框
              this.getUserList(); // 刷新用户列表
            } else {
              this.$message({
                message: '用户添加失败: ' + response.data.message,
                type: 'error',
              });

            }
          })
          .catch((error) => {
            console.error('添加用户时出错:', error);
            this.$message({
              message: '添加用户时出错，请稍后重试',
              type: 'error',
            });
          });
      } else {
        console.log('表单验证失败');
        return false;
      }
    });
  },
 
    handleEdit(row) {
      console.log(row);
      this.dialogStatus = "edit";
      this.addTemp = { ...row };
      this.editId = row.id;
      this.addDialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["addDataForm"].clearValidate(); // 清空表单验证状态
      });
    },
    editData() {
      const columns = this.filteredColumns.map((column) => ({
        name: column,
        value: this.addTemp[column] || "", // 如果没有填写内容，则设置为''
      }));
      const formData = new FormData();
      formData.append("tableName", this.showTableName);
      formData.append("id", this.editId);
      formData.append("columns", JSON.stringify(columns));
      axios
        .post("/api/table/update", formData)
        .then((response) => {
          this.$message.success("更新成功");
          this.fetchTableData(this.showTableName);
          this.addDialogFormVisible = false;
        })
        .catch((error) => {
          console.error(error);
          this.addDialogFormVisible = false;
        });
    },
    handleBatchDelete() {
      if (this.tableData.selection.length === 0) {
        this.$message.warning('请选择要删除的用户')
        return
      }
      const userIds = this.tableData.selection.map(item => item.id)
      this.handleDelete(userIds)
    },
    handleDelete(row) {
      this.$confirm("确定要删除这一行吗？", "警告", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const formData = new FormData();
          formData.append("tableName", this.showTableName);
          formData.append("row_id", row.id);

          axios
            .post("/api/table/delete", formData) // 假设 'id' 是唯一标识符
            .then((response) => {
              this.$message.success("删除成功");
              this.fetchTableData(this.showTableName); // 刷新表格数据
            })
            .catch((error) => {
              console.error("删除行时出错:", error);
              this.$message.error("删除失败");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    handleSwitch() {},
  },
};
</script>

<style lang="scss" scoped>
.user-container {
  margin: 10px;
}
.filter-container {
  margin-top: 30px;
}
</style>
