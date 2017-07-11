import { requireNativeComponent, View, NativeModules, findNodeHandle } from 'react-native';
import React from 'react'
import PropTypes from 'prop-types';

class Amap extends React.Component {
  render() {
    return (
      <RNAmap 
        ref={(ref) => this.ref = ref}
        {...this.props}
      />
    )
  }

  addPoint(obj) {

    NativeModules.AmapModule.addPoint(findNodeHandle(this.ref), obj);
  }
  getPath(start, end) {
    // NativeModules.Amap.getRoutePath(start, end);
  }

  initMapOption(obj) {
    // NativeModules.Amap.initMapOption(findNodeHandle(this.ref), obj);
  }

}
Amap.propTypes = {
  ...View.propTypes
};
const RNAmap = requireNativeComponent('AmapView', Amap, {
  nativeOnly: {
    onChange: true
  }
});

// console.log('RNAmap....', RNAmap);

module.exports = Amap;