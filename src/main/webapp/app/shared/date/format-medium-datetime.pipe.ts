import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDatetime',
})
export default class FormatMediumDatetimePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    // Display timestamps in Bangladesh time (GMT+6) regardless of the browser timezone
    return day ? dayjs(day).utcOffset(360).format('D MMM YYYY HH:mm:ss') : '';
  }
}
