// pages/me/updateMe/updateMe.js
const app = getApp();
var api = require('../../../util/api.js')
var util = require('../../../util/util.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
  },
  updateTo298: function () {
    wx.showModal({
      title: '确认升级',
      content: '你确认以¥298的价格升级为298用户吗？',
      success: (res)=>{
        if (res.confirm)
          api.updateMe(app.getOpenid(), '298', 298, util.getTodayDate())
      }
    })
  },
  updateTo998: function () {
    wx.showModal({
      title: '确认升级',
      content: '你确认以¥998的价格升级为998用户吗？',
      success: (res) => {
        if (res.confirm)
          api.updateMe(app.getOpenid(), '998', 998, util.getTodayDate())
      }
    })

    
  },
  updateToEnterprise: function () {
    wx.showModal({
      title: '确认升级',
      content: '你确认升级为企业用户吗？',
      success: (res) => {
        if (res.confirm)
          api.setMyUserAsEnterprise(app.getOpenid())
      }
    })
    
  }
})