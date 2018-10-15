// pages/me/me.js
const app = getApp();
var api = require('../../util/api.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    myInfo: {
      username: 'USERNAME',
      medals: [
        '../../default/default-icon.png',
        '../../default/default-icon.png',
        '../../default/default-icon.png',
        '../../default/default-icon.png'],
      phone: '123456789',
      email: '123456789@163.com',
      company: '美国永辉有限公司',
      department: 'IT技术部',
      position: 'IT初级经理',
      intro: '我要在代码的世界里飞翔。'
    },
    isEnterprise: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function (options) {
    var that = this
    //获取个人信息
    api.getMyInfo.call(this, app.getOpenid(), () => {
      //检查是否为企业用户
      api.isEnterprise.call(that, app.getOpenid())
    })
  },

  //发布信息
  onPublish: function () {
    console.log('publish')
    wx.navigateTo({
      url: 'publishMyArticle/publishMyArticle',
    })
  },

  //递名片
  /*
  onSendMe: function () {
    wx.navigateTo({
      url: 'myCardHolder/myCardHolder',
    })
  }
  */
  onShareAppMessage: function () {
    var that = this;
    var userId = app.getOpenid();
    return {
      title: '我的钧融圈名片分享',
      path: '/pages/me/myHistory/myHistory?id=' + userId,
      success: function (res) {
        console.log("转发成功" + res);
      }
    }
  }
})