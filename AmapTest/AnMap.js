import { requireNativeComponent, View, NativeModules, findNodeHandle } from 'react-native';
import React from 'react'
import PropTypes from 'prop-types';

class Amap extends React.Component {
  render() {
    console.log(this.props);
    return (
      <RNAmap 
        ref={(ref) => this.ref = ref}
        {...this.props}
        onCusChange={this.props._onChange}
        onCurLocationChange={this.props._onCurLocationChange}
      />
    )
  }
  
  _onCurLocationChange = (event) => {
    // console.log('_onCurLocationChange', event.nativeEvent);
    let { lng, lat } = event.nativeEvent;
    // this.addCurLocation({lng, lat});
  }

  addPoint(obj) {

    NativeModules.AmapModule.addPoint(findNodeHandle(this.ref), obj);
  }

  addPoints(points) {
    NativeModules.AmapModule.addPoints(findNodeHandle(this.ref), points);
  }

  addCurLocation(point) {
    NativeModules.AmapModule.addCurLocation(findNodeHandle(this.ref), point);
  }

  locate(callback) {
    NativeModules.AmapModule.locate(findNodeHandle(this.ref), callback);
  }
  getPath(start, end) {
    NativeModules.AmapModule.getRoutePath(start, end);
    // NativeModules.Amap.getRoutePath(start, end);
  }

  drawPolyline(paths) {
    NativeModules.AmapModule.drawPolyline(findNodeHandle(this.ref), paths);
  }

  initMapOption(obj) {
    // NativeModules.Amap.initMapOption(findNodeHandle(this.ref), obj);
  }


}
Amap.propTypes = {
  ...View.propTypes,
  _onChange: PropTypes.func,
  _onCurLocationChange: PropTypes.func
};
const RNAmap = requireNativeComponent('AmapView', Amap, {
  nativeOnly: {
    onCusChange: true,
    onCurLocationChange: true
  }
});

// console.log('RNAmap....', RNAmap);

module.exports = Amap;