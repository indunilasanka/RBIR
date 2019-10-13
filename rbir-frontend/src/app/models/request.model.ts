/**
 * Created by EMS on 8/24/2017.
 */
import {UserModel} from '../models/user.model';

export enum Authentication {
  AUTHENTICATED,
  REJECTED,
  REQUESTED,
  NOTNEEDED,
}

export class RequestModel {
  public user: UserModel;
  public authenticator: UserModel;
  public header: string;
  public content: string;
  public attachment: File;
  public authentication: Authentication;
  public finished: boolean;
}
