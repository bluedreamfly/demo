import {
  NativeModules
} from 'react-native'


export default async (taskData) => {
  NativeModules.AmapModule.cusLog();
  console.log('taskData', taskData);
}