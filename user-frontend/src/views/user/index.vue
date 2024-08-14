<template>
  <div class="user-container">
    <div class="filter-container">
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        Search
      </el-button>

      <el-button
        class="filter-item"
        style="margin-left: 10px"
        type="success"
        icon="el-icon-edit"
        @click="handleAdd"
      >
        Add
      </el-button>
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
      @selection-change="val => tableData.selection = val"
      @sort-change="handleSortChange"
    >
      <el-table-column type="index" width="60" />
      <el-table-column type="selection" width="50" />
      <el-table-column width="50">
        <template slot-scope="scope">
          <img :id="'avatar-' + scope.row.id" class="table-avatar">
        </template>
      </el-table-column>
      <el-table-column prop="user_name" label="用户名" sortable="custom" />
      <el-table-column prop="true_name" label="真实姓名" sortable="custom" />
      <el-table-column prop="role_list" label="角色" sortable="custom" />
      <el-table-column prop="create_time" label="创建时间" sortable="custom" />
      <el-table-column prop="status" label="是否激活" sortable="custom" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleSwitch(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">
            编辑 </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: red;" @click="handleDelete([scope.row.id])">
            删除</el-button>
        </template>
      </el-table-column>
    </el-table>

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
    <el-dialog
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
        userName: '',
        minCreateTime: '',
        maxCreateTime: '',
        orderBy: '',
        orderMethod: 'asc',
        selection: []
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
      addDialogFormVisible: false,
      dialogStatus: "", // 'add' or 'edit'
      addTemp: {},
      addRules: {},
      addColumns: [],
      textMap: {
        add: "新增表格内容",
        edit: "修改表格内容",
      },
      editId: "",
    };
  },
  created() {
    this.fetchTableData();
  },
  computed: {
    filteredColumns() {
      return this.showTableColumns.filter((column) => column !== "id");
    },
  },
  methods: {
    handleFilter() {
      this.fetchTableData(this.listQuery.tableName);
    },
    tableReset() {
      axios
        .get("/api/tables")
        .then((response) => {
          this.showTableOptions = response.data.map((tableName) => ({
            value: tableName,
            label: tableName,
          }));

          //默认第一个表格内容初始化页面
          if (this.showTableOptions.length > 0) {
            this.showTableName = this.showTableOptions[0].value;
            this.fetchTableData(this.showTableName);
          }
        })
        .catch((error) => {
          console.error("Error fetching tables:", error);
        });
    },
    fetchTableData() {
   

      this.listLoading = true;

      console.log("waiting...");
      const params = {
        searchContent: "",
        pageNum: 1,
        pageSize: 5,
      };
      axios
      .get(`/api/users/getUserList`, { params })
        .then((response) => {
          console.log(response.data.data);
         this.tableData.list=response.data.data
          this.listLoading = false;
        })
        .catch((error) => {
          console.error("There was an error fetching the table data:", error);
          this.listLoading = false;
        });
    },
    sortChange(data) {
      const { prop, order } = data;
      // if (prop === 'id') {
      //   this.sortByID(order)
      // }
    },
    createTable() {
      this.listLoading = true;

      // const payload = {
      //   tableA: "关联测试",
      //   tableB: "南开大学-专利0517",
      //   newTable: "专利-关联",
      //   columnsA: ["序号", "状态", "描述"],
      //   columnsB: ["申请号", "专利名称"],
      //   joinColumn: "序号",
      // };
      const formData = new FormData();
      formData.append("tableA", this.temp.tableA);
      formData.append("tableB", this.temp.tableB);
      formData.append("newTable", this.temp.newTable);

      this.temp.columnsA.forEach((column) => {
        formData.append("columnsA[]", column);
      });

      this.temp.columnsB.forEach((column) => {
        formData.append("columnsB[]", column);
      });

      formData.append("joinColumn", this.temp.joinColumn);

      axios
        .post("/api/table/create", formData)
        .then((response) => {
          this.showTableName = this.temp.newTable;
          this.showTableColumns = response.data.columns;
          this.showTableList = response.data.data;
          this.listLoading = false;
        })
        .catch((error) => {
          console.error(error);
          this.listLoading = false;
        });
    },
    resetTemp() {
      this.temp = {
        tableA: "",
        tableB: "",
        newTable: "",
        columnsA: [],
        columnsB: [],
        joinColumn: "",
      };
      this.tableOptions = [];
      this.ColumnAoptions = [];
      this.ColumnBoptions = [];
    },
    resetaddTemp() {
      //重置add表格的内容（清空）
      this.addTemp = {};
    },
    handleCreate() {
      this.resetTemp(); // 重置表单数据
      this.dialogStatus = "create";
      this.createDialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate(); // 清空表单验证状态
      });
      axios
        .get("/api/tables")
        .then((response) => {
          this.tableOptions = response.data.map((tableName) => ({
            value: tableName,
            label: tableName,
            disabled: false,
          }));
        })
        .catch((error) => {
          console.error("Error fetching tables:", error);
        });
    },

    fetchColumns(tableType) {
      let tableName = tableType === "A" ? this.temp.tableA : this.temp.tableB;
      if (tableName) {
        axios
          .get(`/api/columns/${tableName}`)
          .then((response) => {
            const columns = response.data.map((column) => ({
              value: column,
              label: column,
              disabled: false,
            }));
            if (tableType === "A") {
              this.ColumnAoptions = columns;
              //清空合并列、表格A选择
              this.temp.joinColumn = "";
              this.temp.columnsA = [];
            } else {
              this.ColumnBoptions = columns;
              this.temp.columnsB = [];
              //如果合并列不为空，更新ColumnBoptions中的 disabled 属性
              const joinColumnValue = this.temp.joinColumn;
              if (joinColumnValue !== "") {
                this.ColumnBoptions.forEach((item) => {
                  if (item.value === joinColumnValue) {
                    item.disabled = true;
                  } else {
                    item.disabled = false;
                  }
                });
              }
            }
            // 更新 tableOptions 中的 disabled 属性
            this.tableOptions.forEach((option) => {
              option.disabled =
                option.value === this.temp.tableA ||
                option.value === this.temp.tableB;
            });
          })

          .catch((error) => {
            console.error(`Error fetching columns for ${tableName}:`, error);
          });
      }
    },
    handleJoinColumnChange() {
      // 清空表格A和表格B的关联列
      this.temp.columnsA = [];
      this.temp.columnsB = [];
      // 禁用同名列选项
      const joinColumnValue = this.temp.joinColumn;
      if (joinColumnValue !== "") {
        this.ColumnAoptions.forEach((item) => {
          if (item.value === joinColumnValue) {
            item.disabled = true;
          } else {
            item.disabled = false;
          }
        });

        this.ColumnBoptions.forEach((item) => {
          if (item.value === joinColumnValue) {
            item.disabled = true;
          } else {
            item.disabled = false;
          }
        });
      } else {
        // 如果没有选择合并列，则重置所有列的 disabled 属性
        this.ColumnAoptions.forEach((item) => {
          item.disabled = false;
        });

        this.ColumnBoptions.forEach((item) => {
          item.disabled = false;
        });
      }
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          // 先验证表格名不重复等
          const exists = this.tableOptions.some(
            (option) => option.value === this.temp.newTable
          );
          if (exists) {
            this.$message.error("表格名已存在，请输入不同的表格名称。");
          } else {
            // 验证 ColumnBoptions 是否存在 joinColumn
            const joinColumnExists = this.ColumnBoptions.some(
              (option) => option.value === this.temp.joinColumn
            );
            if (!joinColumnExists) {
              this.$message.error(
                "表格B关联列中不存在选定的合并列，请重新选择。"
              );
            } else {
              this.createTable();
              this.createDialogFormVisible = false;
            }
          }
        } else {
          // 验证不通过的处理逻辑，例如给出错误提示
        }
      });
    },
    handleAdd() {
      this.resetaddTemp(); // 重置表单数据
      this.dialogStatus = "add";
      this.addDialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["addDataForm"].clearValidate(); // 清空表单验证状态
      });
    },
    addData() {
      const columns = this.filteredColumns.map((column) => ({
        name: column,
        value: this.addTemp[column] || "", // 如果没有填写内容，则设置为''
      }));

      const formData = new FormData();
      formData.append("tableName", this.showTableName);
      formData.append("columns", JSON.stringify(columns));

      axios
        .post("/api/table/add", formData)
        .then((response) => {
          this.$message.success("添加成功");
          this.fetchTableData(this.showTableName);
          this.addDialogFormVisible = false;
        })
        .catch((error) => {
          console.error(error);
          this.addDialogFormVisible = false;
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
    handleSwitch(){

    }
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
