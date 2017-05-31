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
    NativeModules.Amap.addPoint(findNodeHandle(this.ref), obj);
  }
}
Amap.propTypes = {
  ...View.propTypes
};
const RNAmap = requireNativeComponent('Amap', Amap);

module.exports = Amap;