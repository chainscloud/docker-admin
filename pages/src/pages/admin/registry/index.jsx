import {PlusOutlined} from '@ant-design/icons';
import {Button, Divider, Modal, Popconfirm} from 'antd';
import React from 'react';
import ProTable from '@ant-design/pro-table';
import http from "@/utils/request";
import {serverUrl} from "@/config";

const addTitle = "添加模板"
const editTitle = '编辑模板'
const deleteTitle = '删除模板'
let api = '/api/registry/';
let prefix = serverUrl;
if (prefix.endsWith("/")) {
  prefix = prefix.substr(0, prefix.length - 1);
}


export default class extends React.Component {

  state = {
    showAddForm: false,
    showEditForm: false,
    formValues: {},
  }
  actionRef = React.createRef();

  columns = [
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '仓库地址',
      dataIndex: 'host',
    },
    {
      title: '命名空间',
      dataIndex: 'namespace',
    },

    {
      title: '账号',
      dataIndex: 'username',
    },
    {
      title: '密码',
      dataIndex: 'password',
      hideInTable: true
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        let menu = <div>
          <a key="1" onClick={() => {
            this.state.showEditForm = true;
            this.state.formValues = record;
            this.setState(this.state)
          }}>修改</a>
          <Divider type="vertical"></Divider>
          <Popconfirm title={'是否确定' + deleteTitle} onConfirm={() => this.handleDelete([record])}>
            <a>删除</a>
          </Popconfirm>
        </div>;


        return menu

      },
    },
  ];
  handleSave = value => {
    http.post(api + 'save', value).then(rs => {
      this.state.showAddForm = false;
      this.setState(this.state)
      this.actionRef.current.reload();
    })
  }

  handleUpdate = value => {
    let params = {...this.state.formValues, ...value};
    http.post(api + 'update', params).then(rs => {
      this.state.showEditForm = false;
      this.setState(this.state)
      this.actionRef.current.reload();
    })

  }

  handleDelete = rows => {
    if (!rows) return true;

    let ids = rows.map(row => row.id);
    http.post(api + 'delete', ids, '删除数据').then(rs => {
      this.actionRef.current.reload();
    })
  }

  render() {
    let {showAddForm, showEditForm} = this.state

    return (<div>


      <div className="panel">
        <a href="https://cr.console.aliyun.com" target="_blank">阿里云镜像仓库</a>
      </div>
      <div className="panel">
        <ProTable
          actionRef={this.actionRef}
          toolBarRender={(action, {selectedRows}) => [
            <Button type="primary" onClick={() => {
              this.state.showAddForm = true;
              this.setState(this.state)
            }}>
              <PlusOutlined/> 新建
            </Button>,
          ]}
          request={(params, sort) => http.getPageableData(api + 'list', params, sort)}
          columns={this.columns}
          rowSelection={false}
          search={false}
          rowKey="id"
        />
      </div>
      <Modal
        maskClosable={false}
        destroyOnClose
        title={addTitle}
        visible={showAddForm}
        onCancel={() => {
          this.state.showAddForm = false;
          this.setState(this.state)
        }}
        footer={null}
      >
        <ProTable
          onSubmit={this.handleSave}
          type="form"
          columns={this.columns}
          rowSelection={false}
        />
      </Modal>


      <Modal
        maskClosable={false}
        destroyOnClose
        title={editTitle}
        visible={showEditForm}
        onCancel={() => {
          this.state.showEditForm = false;
          this.setState(this.state)
        }}
        footer={null}
      >
        <ProTable
          onSubmit={this.handleUpdate}
          form={{initialValues: this.state.formValues}}
          type="form"
          columns={this.columns}
          rowSelection={false}
        />
      </Modal>

    </div>)
  }


}



