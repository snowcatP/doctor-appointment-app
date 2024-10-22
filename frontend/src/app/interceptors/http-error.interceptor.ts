import { HttpErrorResponse, HttpEvent, HttpInterceptorFn, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { inject } from '@angular/core';
import { error, isPlainObject } from 'jquery';
import { MessageService } from 'primeng/api';
import { catchError, tap, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError(error =>{
      console.log(error)
      if(error.status != 200){
          messageService.add({key:'messageToast' ,severity: 'error',summary:'Error', detail: `${error.error.message}` })
      }
      return throwError(error)
    })
  );
};
function checkInvalid200Response(httpEvent: HttpEvent<any>): boolean {
  return (
    httpEvent instanceof HttpResponse
    // Must have a successful status code (200 OK)
    && httpEvent.status === HttpStatusCode.Ok
    // But the body format must be invalid
    && !check200ResponseBodyFormat(httpEvent)
  )
}
function check200ResponseBodyFormat(response: HttpResponse<any>): boolean {
  return isPlainObject(response.body)
    && response.body.status === 'ok'
    && response.body.data !== undefined
}
function checkNoNetworkConnection(error: any): boolean {
  return(
    error instanceof HttpErrorResponse
    && !error.headers.keys().length
    && !error.ok
    && !error.status
    && !error.error.loaded
    && !error.error.total
  )
}
function isResponseError(error: any) {
  return (error instanceof HttpErrorResponse && error.status !== HttpStatusCode.Ok);
}