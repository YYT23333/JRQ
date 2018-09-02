// pages/articleDetail/projectDetail/projectDetail.js
const app = getApp()
var api = require('../../../util/api.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    project: {
      id: 1, //编号
      title: '这是一个页面', //简介
      identity: 'IT初级经理', //身份
      phone: '123456789', //联系方式
      city: '亚太区', //所在城市
      industry: 'IT', //所属行业
      business: '金融牌照', //业务标签
      content: '这是一个很牛逼的项目', //项目详情
      money: 2333, //项目资金
      attachment: 'http://www.baidu.com/test.zip' //附件路径
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    api.getProject(options.id, this)
  },
  onDownload: function () {
    api.downloadFile(this.data.project.attachment)
  }
})