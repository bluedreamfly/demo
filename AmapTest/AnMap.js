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
      />
    )
  }
  
  

  addPoint(obj) {

    NativeModules.AmapModule.addPoint(findNodeHandle(this.ref), obj);
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
  _onChange: PropTypes.func
};
const RNAmap = requireNativeComponent('AmapView', Amap, {
  nativeOnly: {
    onCusChange: true
  }
});

// console.log('RNAmap....', RNAmap);

module.exports = Amap;