import { Directive, ElementRef, Input, HostListener, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import * as CountUp from 'countup.js';
import { isPlatformBrowser } from '@angular/common';

@Directive({
  selector: '[vrCountUp]'
})
export class CountUpDirective implements OnInit {


  /**
   * Optional extra configuration, such as easing.
   */
  // noinspection TsLint
  @Input('vrCountUp') options: any;

  /**
   * Optional start value for the count. Defaults to zero.
   */
  @Input()
  startVal: number;

  /**
   * The value to count up to.
   */
  private _endVal: number;
  @Input()
  get endVal(): number {
    return this._endVal;
  }

  //noinspection JSUnusedGlobalSymbols
  set endVal(value: number) {
    this._endVal = value;

    if (isNaN(value)) {
      return;
    }

    if (!this._countUp) {
      return;
    }

    this._countUp.update(value);
  }

  /**
   * Optional duration of the animation. Default is two seconds.
   */
  @Input()
  duration: number;

  /**
   * Optional number of decimal places. Default is two.
   */
  @Input()
  decimals: number;

  /**
   * Optional flag for specifying whether the element should re-animate when clicked.
   * Default is true.
   */
  @Input()
  reanimateOnClick: boolean;

  ngOnInit() {
    this._countUp = this.createCountUp(
      this.startVal, this.endVal, this.decimals, this.duration);
    this.animate();
  }

  //noinspection JSUnusedGlobalSymbols
  /**
   * Re-animate if preference is set.
   */
  @HostListener('click')
  onClick() {
    if (this.reanimateOnClick) {
      this.animate();
    }
  }

  //noinspection TsLint
  private _countUp;

  constructor(@Inject(ElementRef) private el: ElementRef, @Inject(PLATFORM_ID) private platformId: any) {}

  private createCountUp(sta, end, dec, dur) {
    if (isPlatformBrowser(this.platformId)) {
      sta = sta || 0;
      //noinspection TsLint
      if (isNaN(sta)) sta = Number(sta.match(/[\d\-\.]+/g).join('')); // strip non-numerical characters
      end = end || 0;
      //noinspection TsLint
      if (isNaN(end)) end = Number(end.match(/[\d\-\.]+/g).join('')); // strip non-numerical characters
      dur = Number(dur) || 2;
      dec = Number(dec) || 0;

      // construct countUp
      let countUp = new CountUp(this.el.nativeElement, sta, end, dec, dur, this.options);
      if (end > 999) {
        // make easing smoother for large numbers
        countUp = new CountUp(this.el.nativeElement, sta, end - 100, dec, dur / 2, this.options);
      }

      return countUp;
    }
  }

  private animate() {
    if (isPlatformBrowser(this.platformId)) {
      this._countUp.reset();
      if (this.endVal > 999) {
        this._countUp.start(() => this._countUp.update(this.endVal));
      } else {
        this._countUp.start();
      }
    }
  }

}
